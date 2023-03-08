package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ManageCategoriesEvent
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    private val addEditFinanceUseCases: AddEditFinanceUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    private var incomeCategories: List<Category> = emptyList()
    private var outcomeCategories: List<Category> = emptyList()

    val formState = FormState(
        fields = listOf(
            TextFieldState(
                name = "name",
                validators = listOf(Validators.Required()),
            ),
            TextFieldState(
                name = "amount",
                validators = listOf(Validators.MinValue(0, "Amount must be higher than 0"), Validators.Required()),
            )
        )
    )

    init {
        getCategories()
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ToggleAddEditCard -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
                if(!_state.value.isAddEditOpen) return
                if(event.finance == null) {
                    _state.value = _state.value.copy(currentType = FinanceType.OUTCOME)
                    selectCategoryType(_state.value.currentType)
                } else {
                    _state.value = _state.value.copy(
                        currentFinanceId = event.finance.finance.id,
                        selectedCategoryId = event.finance.finance.financeCategoryId,
                        currentType = event.finance.finance.type,
                        timestamp = event.finance.finance.timestamp
                    )
                    formState.fields[0].change(event.finance.finance.name)
                    formState.fields[1].change(event.finance.finance.amount.toInt().toString())
                }
            }
            HomeEvent.ToggleType -> {
                _state.value = _state.value.copy(
                    currentType = FinanceType[(_state.value.currentType.value + 1) % 2]!!,
                )
                selectCategoryType(_state.value.currentType)
            }
            is HomeEvent.CategorySelected -> {
                _state.value.categoryStates.forEach { (id, _) ->
                    _state.value.categoryStates[id]?.value = id == event.id
                }
            }
            is HomeEvent.DateChanged -> {
                _state.value = _state.value.copy(timestamp = event.timestamp)
            }
            HomeEvent.AddFinance -> {
                viewModelScope.launch {
                    addEditFinanceUseCases.add(FinanceItem(
                        name = formState.fields[0].value,
                        amount = (formState.fields[1].value).toDouble(),
                        timestamp = _state.value.timestamp,
                        type = _state.value.currentType,
                        id = _state.value.currentFinanceId,
                        financeCategoryId = _state.value.selectedCategoryId!!,
                        financeAccountId = 1
                    ))
                    onEvent(HomeEvent.ToggleAddEditCard(null))
                }
            }
        }
    }

    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.getCategories()
            .onEach {
                incomeCategories = it.filter { it.type == FinanceType.INCOME }
                outcomeCategories = it.filter { it.type == FinanceType.OUTCOME }
                if(incomeCategories.isNotEmpty() && outcomeCategories.isNotEmpty()) selectCategoryType(FinanceType.OUTCOME)
            }
            .launchIn(viewModelScope)
    }
    private fun selectCategoryType(type: FinanceType) {
        when(type) {
            FinanceType.INCOME -> populateCategoryList(incomeCategories)
            FinanceType.OUTCOME -> populateCategoryList(outcomeCategories)
        }
    }
    private fun populateCategoryList(list: List<Category>) {
        _state.value = _state.value.copy(
            categories = list,
            selectedCategoryId = list[0].categoryId,
            categoryStates = HashMap()
        )
        list.forEach { category ->
            category.categoryId?.let { id -> _state.value.categoryStates[id] = mutableStateOf(category.categoryId == _state.value.selectedCategoryId) }
        }
    }
}