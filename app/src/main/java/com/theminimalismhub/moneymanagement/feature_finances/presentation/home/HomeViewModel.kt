package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.utils.RangePickerService
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    private val addEditFinanceUseCases: AddEditFinanceUseCases
) : ViewModel() {

    val addEditService = AddEditFinanceService(viewModelScope, addEditFinanceUseCases)
    fun onEvent(event: AddEditFinanceEvent) { addEditService.onEvent(event) }

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private var selectedCategoryId: Int? = null
    val rangeService = RangePickerService()

    init {
        initDateRange()
        getFinances()
        getCategoryTotals()
        getAccounts()
    }
    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ToggleAddEditCard -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
                if(!_state.value.isAddEditOpen) return
                addEditService.onEvent(AddEditFinanceEvent.ToggleAddEditCard(event.finance))
            }
            is HomeEvent.RangeChanged -> {
                toggleCategoryBar(selectedCategoryId)
                _state.value = _state.value.copy(dateRange = event.range)
                getFinances()
                getCategoryTotals()
            }
            is HomeEvent.CategoryClicked -> {
                toggleCategoryBar(event.id)
                getFinances()
            }
            is HomeEvent.ItemTypeSelected -> {
                selectedCategoryId = null
                _state.value.itemsTypeStates.forEach { it -> _state.value.itemsTypeStates[it.key]!!.value = it.key == event.idx }
                getFinances()
                getCategoryTotals()
            }
        }
    }

    // Finances
    private var getFinancesJob: Job? = null
    private fun getFinances() {
        val types: MutableList<FinanceType>
        val tracked: MutableList<Boolean>
        val idx = _state.value.itemsTypeStates.filter { it.value.value }.entries.first().key
        types = if(idx == 0 || idx == 3) mutableListOf(FinanceType.OUTCOME, FinanceType.INCOME) else if (idx == 1) mutableListOf(FinanceType.OUTCOME) else mutableListOf(FinanceType.INCOME)
        tracked = if(idx == 0) mutableListOf(true, false) else if (idx == 1 || idx == 2) mutableListOf(true) else mutableListOf(false)
        getFinancesJob?.cancel()
        getFinancesJob = useCases.getFinances(_state.value.dateRange, selectedCategoryId, types, tracked)
            .onEach { finance ->
                _state.value = _state.value.copy(
                    results = finance
                )
                getGraphData(selectedCategoryId)
            }
            .launchIn(viewModelScope)
    }

    // Overview
    private fun initDateRange() {
        _state.value = _state.value.copy(
            dateRange = Pair(rangeService.getStartTimestamp(), rangeService.getEndTimestamp())
        )
    }

    private var getPerCategoryJob: Job? = null
    private fun getCategoryTotals() {
        val idx = _state.value.itemsTypeStates.filter { it.value.value }.entries.first().key
        val type = if(idx == 2) FinanceType.INCOME else FinanceType.OUTCOME
        getPerCategoryJob?.cancel()
        getPerCategoryJob = useCases.getTotalPerCategory(_state.value.dateRange, type)
            .onEach { totals ->
                _state.value = _state.value.copy(
                    totalPerCategory = totals.sortedBy { it.amount }.reversed()
                )
                _state.value = _state.value.copy(
                    totalsPerAccount = totals.groupBy { it.accountId }
                )

                totals.forEach {
                    _state.value.categoryBarStates[it.categoryId] = mutableStateOf(
                        when (selectedCategoryId) {
                            null -> CategoryBarState.NEUTRAL
                            it.categoryId -> CategoryBarState.SELECTED
                            else -> CategoryBarState.DESELECTED
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    private fun toggleCategoryBar(categoryId: Int?) {
        if(selectedCategoryId == categoryId) {
            _state.value.categoryBarStates.forEach { (_, state) -> state.value = CategoryBarState.NEUTRAL }
            selectedCategoryId = null
        } else {
            _state.value.categoryBarStates.forEach { (id, state) -> state.value = if(id == categoryId) CategoryBarState.SELECTED else CategoryBarState.DESELECTED }
            selectedCategoryId = categoryId
        }
    }
    private suspend fun getGraphData(categoryId: Int? = null) {
        if(rangeService.rangeLength == 1) return
        _state.value = _state.value.copy(
            earningsPerTimePeriod = useCases.getTotalPerCategory.getPerDay(
                range = _state.value.dateRange,
                type = FinanceType.OUTCOME,
                categoryId = categoryId
            )
        )
        if(categoryId == null) {
            _state.value = _state.value.copy(
                maxEarnings = _state.value.earningsPerTimePeriod.maxOf { it.value }
            )
        }
    }

    // Accounts
    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAccounts()
            .onEach {
                _state.value = _state.value.copy(
                    accounts = it
                )
                addEditService.setAccounts(it)
            }
            .launchIn(viewModelScope)
    }
}