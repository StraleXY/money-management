package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import androidx.compose.runtime.MutableState
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
    private val state: MutableState<AddEditFinanceState>,
    private val scope: CoroutineScope,
    private val useCases: AddEditFinanceUseCases
) {

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

    fun onEvent(event: AddEditFinanceEvent) {
        when(event) {
            is AddEditFinanceEvent.ToggleAddEditCard -> {
                if(event.finance == null) {
                    state.value = state.value.copy(currentType = FinanceType.OUTCOME)
                    selectCategoryType(state.value.currentType)
                } else {
                    state.value = state.value.copy(
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
                state.value = state.value.copy(
                    currentType = FinanceType[(state.value.currentType.value + 1) % 2]!!,
                )
                selectCategoryType(state.value.currentType)
            }
            is AddEditFinanceEvent.CategorySelected -> {
                state.value.categoryStates.forEach { (id, _) ->
                    state.value.categoryStates[id]?.value = id == event.id
                }
            }
            is AddEditFinanceEvent.DateChanged -> {
                state.value = state.value.copy(timestamp = event.timestamp)
            }
            AddEditFinanceEvent.AddFinance -> {
                scope.launch {
                    useCases.add(
                        FinanceItem(
                            name = formState.fields[0].value,
                            amount = (formState.fields[1].value).toDouble(),
                            timestamp = state.value.timestamp,
                            type = state.value.currentType,
                            id = state.value.currentFinanceId,
                            financeCategoryId = state.value.selectedCategoryId!!,
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
        state.value = state.value.copy(
            categories = list,
            selectedCategoryId = list[0].categoryId,
            categoryStates = HashMap()
        )
        list.forEach { category ->
            category.categoryId?.let { id -> state.value.categoryStates[id] = mutableStateOf(category.categoryId == state.value.selectedCategoryId) }
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