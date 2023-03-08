package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.HashMap

class AddEditFinanceService(
    private val scope: CoroutineScope,
    private val useCases: AddEditFinanceUseCases
) {

    private val _state = mutableStateOf(AddEditFinanceState())
    val state: State<AddEditFinanceState> = _state

    private var incomeCategories: List<Category> = emptyList()
    private var outcomeCategories: List<Category> = emptyList()

    init {
        getCategories()
    }

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

    fun onEvent(event: AddEditFinanceEvent) {
        when(event) {
            is AddEditFinanceEvent.ToggleAddEditCard -> {
                if(event.finance == null) {
                    _state.value = _state.value.copy(
                        currentType = FinanceType.OUTCOME,
                        timestamp = System.currentTimeMillis()
                    )
                    selectCategoryType(_state.value.currentType)
                    formState.fields[0].change("")
                    formState.fields[1].change("")
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
            AddEditFinanceEvent.ToggleType -> {
                _state.value = _state.value.copy(
                    currentType = FinanceType[(_state.value.currentType.value + 1) % 2]!!,
                )
                selectCategoryType(_state.value.currentType)
            }
            is AddEditFinanceEvent.CategorySelected -> {
                _state.value.categoryStates.forEach { (id, _) ->
                    _state.value.categoryStates[id]?.value = id == event.id
                }
            }
            is AddEditFinanceEvent.DateChanged -> {
                _state.value = _state.value.copy(timestamp = event.timestamp)
            }
            AddEditFinanceEvent.AddFinance -> {
                scope.launch {
                    useCases.add(
                        FinanceItem(
                            name = formState.fields[0].value,
                            amount = (formState.fields[1].value).toDouble(),
                            timestamp = _state.value.timestamp,
                            type = _state.value.currentType,
                            id = _state.value.currentFinanceId,
                            financeCategoryId = _state.value.selectedCategoryId!!,
                            financeAccountId = 1
                        )
                    )
                }
            }
        }
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
    private var getCategoriesJob: Job? = null
    fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.getCategories()
            .onEach {
                incomeCategories = it.filter { it.type == FinanceType.INCOME }
                outcomeCategories = it.filter { it.type == FinanceType.OUTCOME }
                if(incomeCategories.isNotEmpty() && outcomeCategories.isNotEmpty()) selectCategoryType(
                    FinanceType.OUTCOME)
            }
            .launchIn(scope)
    }
}