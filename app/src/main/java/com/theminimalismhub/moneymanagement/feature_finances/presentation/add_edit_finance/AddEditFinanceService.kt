package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
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
    private var initialAccountId: Int? = null
    private var initialAmount: Double = 0.0

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
                        currentFinanceId = null,
                        timestamp = System.currentTimeMillis()
                    )
                    selectCategoryType(_state.value.currentType)
                    formState.fields[0].change("")
                    formState.fields[1].change("")
                    onEvent(AddEditFinanceEvent.AccountSelected(_state.value.accounts.find { it.primary }?.accountId!!))
                    initialAccountId = null
                    initialAmount = 0.0
                } else {
                    _state.value = _state.value.copy(
                        currentFinanceId = event.finance.finance.id,
                        currentType = event.finance.finance.type,
                        timestamp = event.finance.finance.timestamp,
                        selectedAccountId = event.finance.finance.financeAccountId
                    )
                    selectCategoryType(_state.value.currentType)
                    onEvent(AddEditFinanceEvent.CategorySelected(event.finance.finance.financeCategoryId!!))
                    formState.fields[0].change(event.finance.finance.name)
                    formState.fields[1].change(event.finance.finance.amount.toInt().toString())
                    onEvent(AddEditFinanceEvent.AccountSelected(event.finance.finance.financeAccountId))
                    initialAccountId = event.finance.finance.financeAccountId
                    initialAmount = event.finance.finance.amount
                }
            }
            is AddEditFinanceEvent.AccountSelected -> {
                _state.value.accountStates.forEach { (id, _) ->
                    _state.value.accountStates[id]?.value = id == event.id
                }
                _state.value = _state.value.copy(
                    selectedAccountId = event.id
                )
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
                _state.value = _state.value.copy(
                    selectedCategoryId = event.id
                )
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
                            financeAccountId = _state.value.selectedAccountId!!
                        )
                    )
                    if (_state.value.currentFinanceId == null) useCases.updateAccountBalance(if(_state.value.currentType == FinanceType.OUTCOME) -(formState.fields[1].value).toDouble() else (formState.fields[1].value).toDouble(), _state.value.selectedAccountId!!)
                    else if (_state.value.currentFinanceId != null && initialAccountId == _state.value.selectedAccountId) {
                        useCases.updateAccountBalance(if (_state.value.currentType == FinanceType.OUTCOME) -((formState.fields[1].value).toDouble() - initialAmount) else ((formState.fields[1].value).toDouble() - initialAmount), _state.value.selectedAccountId!!)
                    }
                    else if (_state.value.currentFinanceId != null && initialAccountId != _state.value.selectedAccountId) {
                        useCases.updateAccountBalance(if(_state.value.currentType == FinanceType.OUTCOME) initialAmount else -initialAmount, initialAccountId!!)
                        useCases.updateAccountBalance(if(_state.value.currentType == FinanceType.OUTCOME) -(formState.fields[1].value).toDouble() else (formState.fields[1].value).toDouble(), _state.value.selectedAccountId!!)

                    }
                }
            }
            AddEditFinanceEvent.DeleteFinance -> {
                scope.launch {
                    useCases.delete(state.value.currentFinanceId!!)
                    useCases.updateAccountBalance(if(_state.value.currentType == FinanceType.INCOME) -(formState.fields[1].value).toDouble() else (formState.fields[1].value).toDouble(), _state.value.selectedAccountId!!)
                }
            }
        }
    }
    private fun selectCategoryType(type: FinanceType) {
        when(type) {
            FinanceType.INCOME -> populateCategoryList(incomeCategories)
            FinanceType.OUTCOME -> populateCategoryList(outcomeCategories)
            else -> {}
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
    private fun getCategories() {
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

    fun setAccounts(accounts: List<Account>) {
        _state.value = _state.value.copy(
            accounts = accounts,
            selectedAccountId = accounts.find { it.primary }?.accountId
        )
        accounts.forEach { account ->
            account.accountId?.let { id -> _state.value.accountStates[id] = mutableStateOf(account.accountId == _state.value.selectedAccountId) }
        }
    }
}