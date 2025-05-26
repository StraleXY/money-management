package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.util.Log
import androidx.compose.material.Colors
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.enums.RangeType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.utils.RangePickerService
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceService
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    private val addEditFinanceUseCases: AddEditFinanceUseCases,
    private val preferences: Preferences
) : ViewModel() {

    val addEditService = AddEditFinanceService(viewModelScope, addEditFinanceUseCases, preferences)
    lateinit var colors: Colors
    fun onEvent(event: AddEditFinanceEvent) { addEditService.onEvent(event) }

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    val rangeService = RangePickerService()
    private var selectedAccountId: Int? = null

    init { init() }

    fun init() {
        _state.value = _state.value.copy(
            limit = preferences.getSimpleLimit().toDouble(),
            currency = preferences.getCurrency(),
            showLineGraph = preferences.getShowLineGraph(),
            collapseCategories = preferences.getCollapseCategories(),
            filterIncomeByAccount = preferences.getFilterIncomeByAccount(),
            filterOutcomeByAccount = preferences.getFilterOutcomeByAccount(),
            swipeableNavigation = preferences.getSwipeableNavigation()
        )
        getCategories()
        getFunds()
        initDateRange()
        initAverages()
        getFinances()
        getRecommended()
        getAccounts()
    }
    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.Init -> {
                colors = event.colors
            }
            is HomeEvent.ToggleAddEditCard -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
                if(!_state.value.isAddEditOpen) return
                addEditService.onEvent(AddEditFinanceEvent.ToggleAddEditCard(event.finance, event.recommended))
            }
            is HomeEvent.RangeChanged -> {
                toggleCategoryBar(_state.value.selectedCategoryId)
                _state.value = _state.value.copy(
                    dateRange = event.range,
                    isToday = event.isToday
                )
                if (rangeService.type == RangeType.DAILY) addEditService.onEvent(AddEditFinanceEvent.DateChanged(event.range.first))
                else addEditService.onEvent(AddEditFinanceEvent.DateChanged(System.currentTimeMillis()))
                getFinances()
            }
            is HomeEvent.CategoryClicked -> {
                toggleCategoryBar(event.id)
                getFinances(false)
            }
            is HomeEvent.ItemTypeSelected -> {
                _state.value = _state.value.copy(selectedCategoryId = null)
                selectedAccountId?.let { toggleAccountPreview(selectedAccountId!!) }
                _state.value.itemsTypeStates.forEach { _state.value.itemsTypeStates[it.key]!!.value = it.key == event.idx }
                getFinances()
            }
            is HomeEvent.AccountClicked -> {
                _state.value = _state.value.copy(selectedCategoryId = null)
                toggleAccountPreview(event.id)
                getFinances()
            }
            is HomeEvent.ToggleShowLineGraph -> {
                _state.value = _state.value.copy(showLineGraph = !_state.value.showLineGraph)
                preferences.setShowLineGraph(_state.value.showLineGraph)
            }
            is HomeEvent.DisplayTypeChanged -> {
                _state.value = _state.value.copy(selectedCategoryId = null)
                selectedAccountId?.let { toggleAccountPreview(selectedAccountId!!) }
                _state.value = _state.value.copy(displayTypes = event.types)
                getFinances()
            }
            is HomeEvent.DisplayTrackedChanged -> {
                _state.value = _state.value.copy(selectedCategoryId = null)
                selectedAccountId?.let { toggleAccountPreview(selectedAccountId!!) }
                _state.value = _state.value.copy(displayTracked = event.tracked)
                getFinances()
            }
            is HomeEvent.DeleteRecommendedFinance -> {
                viewModelScope.launch { useCases.deleteFinance.recommended(event.id) }
            }
            is HomeEvent.PayRecommendedFinance -> {
                val account = event.recommended.account ?: _state.value.accounts.first { it.primary }
                val category = event.recommended.category ?: _state.value.categories.first { it.type == event.recommended.recommended.type }
                onEvent(HomeEvent.ToggleAddEditCard(Finance(
                    finance = FinanceItem(
                        name = event.recommended.recommended.placeName,
                        amount = event.recommended.recommended.amount,
                        timestamp = event.recommended.recommended.timestamp,
                        type = event.recommended.recommended.type,
                        financeCategoryId = category.categoryId,
                        financeAccountId = account.accountId!!
                    ),
                    category = category,
                    account = account,
                    accountTo = null
                ), event.recommended))
            }
        }
    }

    // Get Categories
    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.getCategories()
            .onEach { _state.value = _state.value.copy(categories = it) }
            .launchIn(viewModelScope)
    }

    // Finances
    private var getFinancesJob: Job? = null
    private fun getFinances(updateQuickSpending: Boolean = true) {
        val types: MutableList<FinanceType> = _state.value.displayTypes.toMutableList()
        val tracked: MutableList<Boolean> = _state.value.displayTracked.toMutableList()

        getFinancesJob?.cancel()
        getFinancesJob = useCases.getFinances(_state.value.dateRange, _state.value.selectedCategoryId, selectedAccountId, types, tracked)
            .onEach { finance ->
                _state.value = _state.value.copy(results = finance)
                if(_state.value.selectedCategoryId == null) getCategoryTotals()
                if(updateQuickSpending) updateQuickSpending()
                getGraphData()
            }
            .launchIn(viewModelScope)
    }
    private var getRecommendedJob: Job? = null
    private fun getRecommended() {
        getRecommendedJob?.cancel()
        getRecommendedJob = useCases.getFinances.recommended()
            .onEach {
                _state.value = _state.value.copy(recommended = it)
                Log.d("Recommended", "$it")
            }
            .launchIn(viewModelScope)
    }
    private fun updateQuickSpending() {
        val types: MutableList<FinanceType> = if(_state.value.displayTypes.contains(FinanceType.OUTCOME)) mutableListOf(FinanceType.OUTCOME) else mutableListOf(FinanceType.INCOME)
        _state.value = _state.value.copy(
            quickSpendingAmount = _state.value.results.sumOf { if (types.contains(it.finance.type)) it.finance.amount else 0.0 }
        )
    }

    // Overview
    private fun initDateRange() {
        _state.value = _state.value.copy(
            dateRange = Pair(rangeService.getStartTimestamp(), rangeService.getEndTimestamp())
        )
    }
    private fun initAverages() {
        viewModelScope.launch {
            val yearRange = Dater.getDateRange(month = null, year = Dater.getYear())
            val yearTotal = useCases.getTotal(yearRange, FinanceType.OUTCOME, null, null, listOf(true))
            _state.value = _state.value.copy(dailyAverage = yearTotal / LocalDateTime.now().dayOfYear)
        }
    }
    private fun getCategoryTotals() {
        val type: FinanceType = if(_state.value.displayTypes.contains(FinanceType.OUTCOME)) FinanceType.OUTCOME else FinanceType.INCOME
        val bars: MutableList<CategoryAmount> = mutableListOf()
        _state.value.results.filter{ it.finance.type == type }.groupBy { it.category!!.categoryId }.forEach {
            bars.add(
                CategoryAmount(
                    categoryId = it.key!!,
                    accountId = it.value[0].account.accountId!!,
                    name = it.value[0].category!!.name,
                    color = it.value[0].category!!.color,
                    amount = it.value.sumOf { it.finance.amount },
                    count = it.value.count()
                )
            )
        }
        _state.value = _state.value.copy(totalPerCategory = bars.sortedBy { it.amount }.reversed())
        _state.value = _state.value.copy(totalsPerAccount = bars.groupBy { it.accountId })
        bars.forEach {
            _state.value.categoryBarStates[it.categoryId] = mutableStateOf(
                when (_state.value.selectedCategoryId) {
                    null -> CategoryBarState.NEUTRAL
                    it.categoryId -> CategoryBarState.SELECTED
                    else -> CategoryBarState.DESELECTED
                }
            )
        }
    }
    private fun toggleCategoryBar(categoryId: Int?) {
        if(_state.value.selectedCategoryId == categoryId) {
            _state.value.categoryBarStates.forEach { (_, state) -> state.value = CategoryBarState.NEUTRAL }
            _state.value = _state.value.copy(selectedCategoryId = null)
        } else {
            _state.value.categoryBarStates.forEach { (id, state) -> state.value = if(id == categoryId) CategoryBarState.SELECTED else CategoryBarState.DESELECTED }
            _state.value = _state.value.copy(selectedCategoryId = categoryId)
        }
    }
    private fun getGraphData() {
        if(rangeService.rangeLength == 1) return
        _state.value = _state.value.copy(
            earningsPerTimePeriod = useCases.getTotalPerDay(
                range = _state.value.dateRange,
                type = if(_state.value.displayTypes.contains(FinanceType.OUTCOME)) FinanceType.OUTCOME else FinanceType.INCOME,
                items = _state.value.results,
                color =
                    if(_state.value.selectedCategoryId == null) colors.onSurface.toArgb()
                    else Colorer.getAdjustedDarkColor(_state.value.totalPerCategory.first{ it.categoryId == _state.value.selectedCategoryId }.color, colors.isLight).toArgb()
            )
        )
        if(_state.value.selectedCategoryId == null) {
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
                _state.value.accounts.forEach { account ->
                    account.accountId?.let { id -> _state.value.accountStates[id] = mutableStateOf(true) }
                }
            }
            .launchIn(viewModelScope)
    }
    private fun toggleAccountPreview(accountId: Int) {
        if(selectedAccountId == accountId) {
            _state.value.accountStates.forEach { (_, state) -> state.value = true }
            selectedAccountId = null
        } else {
            _state.value.accountStates.forEach { (id, state) -> state.value = id == accountId }
            selectedAccountId = accountId
        }
    }

    // Budgets
    private var getFundsJob: Job? = null
    private fun getFunds() {
        getFundsJob?.cancel()
        getFundsJob = useCases.getFunds()
            .onEach {
                _state.value = _state.value.copy(funds = it)
                addEditService.setFunds(it)
            }
            .launchIn(viewModelScope)
    }
}