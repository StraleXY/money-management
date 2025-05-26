package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddEditFinanceService(
    private val scope: CoroutineScope,
    private val useCases: AddEditFinanceUseCases,
    preferences: Preferences
) {

    private val _state = mutableStateOf(AddEditFinanceState())
    val state: State<AddEditFinanceState> = _state

    private var incomeCategories: List<Category> = emptyList()
    private var outcomeCategories: List<Category> = emptyList()
    private var initialAccountId: Int? = null
    private var initialAmount: Double = 0.0
    private var selectedRecommendedFinance: RecommendedFinance? = null

    init {
        getCategories()
        _state.value = _state.value.copy(currency = preferences.getCurrency())
    }

    val formState = FormState(
        fields = listOf(
            TextFieldState(
                name = "name",
                validators = listOf(Validators.Required()),
            ),
            TextFieldState(
                name = "amount",
                validators = listOf(Validators.Required()), //Validators.MinValue(0, "Amount must be higher than 0"),
            )
        )
    )

    fun onEvent(event: AddEditFinanceEvent) {
        when(event) {
            is AddEditFinanceEvent.ToggleAddEditCard -> {
                if(event.finance == null) {
                    selectedRecommendedFinance = null
                    _state.value = _state.value.copy(
                        currentType = FinanceType.OUTCOME,
                        currentFinanceId = null,
                        linkedFundId = null,
                        selectedCategoryId = null
                    )
                    selectCategoryType(_state.value.currentType)
                    formState.fields[0].change("")
                    formState.fields[1].change("")
                    onEvent(AddEditFinanceEvent.AccountSelected(_state.value.accounts.find { it.primary }?.accountId!!))
                    initialAccountId = null
                    initialAmount = 0.0
                } else {
                    selectedRecommendedFinance = event.recommended
                    selectCategoryType(event.finance.category!!.type)
                    event.finance.finance.financeCategoryId?.let { onEvent(AddEditFinanceEvent.CategorySelected(it)) }
                    _state.value = _state.value.copy(
                        currentFinanceId = event.finance.finance.financeId,
                        currentType = event.finance.finance.type,
                        timestamp = event.finance.finance.timestamp,
                        selectedAccountId = event.finance.finance.financeAccountId,
                        currentTrackable = event.finance.finance.trackable
                    )
                    formState.fields[0].change(event.finance.finance.name)
                    formState.fields[1].change(Currencier.formatAmount(event.finance.finance.amount, false))
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
            is AddEditFinanceEvent.ToggleType -> {
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
                    selectedCategoryId = event.id,
                    currentTrackable = _state.value.categories.first { it.categoryId == event.id }.trackable
                )
            }
            is AddEditFinanceEvent.DateChanged -> {
                _state.value = _state.value.copy(timestamp = event.timestamp)
            }
            is AddEditFinanceEvent.TrackableToggled -> {
                _state.value = _state.value.copy(
                    currentTrackable = !_state.value.currentTrackable
                )
            }
            is AddEditFinanceEvent.AddFinance -> {
                scope.launch {
                    var item = FinanceItem(
                        name = formState.fields[0].value,
                        amount = (formState.fields[1].value).toDouble(),
                        timestamp = _state.value.timestamp,
                        type = _state.value.currentType,
                        financeId = _state.value.currentFinanceId,
                        financeCategoryId = _state.value.selectedCategoryId!!,
                        financeAccountId = _state.value.selectedAccountId!!,
                        trackable = _state.value.currentTrackable
                    )
                    val financeId = useCases.add(item)
                    item = item.copy(financeId = financeId)
                    handleRecommendedFinance(financeId)
                    handleLinkedBudget(item)
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
            is AddEditFinanceEvent.DeleteFinance -> {
                scope.launch {
                    useCases.delete(state.value.currentFinanceId!!)
                    useCases.updateAccountBalance(if(_state.value.currentType == FinanceType.INCOME) -(formState.fields[1].value).toDouble() else (formState.fields[1].value).toDouble(), _state.value.selectedAccountId!!)
                }
            }
            is AddEditFinanceEvent.SelectBudget -> { _state.value = _state.value.copy(linkedFundId = event.budget?.item?.fundId) }
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
            categoryStates = HashMap(),
            currentTrackable = list[0].trackable
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
                if(incomeCategories.isNotEmpty() && outcomeCategories.isNotEmpty()) selectCategoryType(FinanceType.OUTCOME)
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
    fun setFunds(funds: List<Fund>) {
        _state.value = _state.value.copy(funds = funds)
    }
    private suspend fun handleRecommendedFinance(financeId: Int) {
        selectedRecommendedFinance?.let {
            useCases.add(it.copy(recommended = it.recommended.copy(financeItemId = financeId)).recommended)
        }
    }
    private suspend fun handleLinkedBudget(item: FinanceItem) {
        if (_state.value.currentFinanceId != null) {
            val linkedFund = _state.value.funds.firstOrNull { it.finances.contains(item) }
            if (linkedFund != null && linkedFund.item.fundId != _state.value.linkedFundId) { unlinkBudget(linkedFund.item.fundId!!, item) }
            _state.value.linkedFundId?.let { linkBudget(it, item) }
        }
        else { _state.value.linkedFundId?.let { linkBudget(it, item) } }
    }
    private suspend fun linkBudget(id: Int, item: FinanceItem) {
        _state.value.funds.firstOrNull { it.item.fundId == id }?.let {
            val finances: MutableList<FinanceItem> = it.finances.toMutableList()
            finances.add(item)
            useCases.addFund(it.copy(finances = finances.toList()))
        }
    }
    private suspend fun unlinkBudget(id: Int, item: FinanceItem) {
        _state.value.funds.firstOrNull { it.item.fundId == id }?.let {
            val finances: MutableList<FinanceItem> = it.finances.toMutableList()
            finances.remove(item)
            useCases.addFund(it.copy(finances = finances.toList()))
        }
    }
}
