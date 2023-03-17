package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.ManageAccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.xml.validation.Validator

@HiltViewModel
class ManageAccountsViewModel @Inject constructor(
    private val useCases: ManageAccountsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ManageAccountsState())
    val state: State<ManageAccountsState> = _state

    val addEditFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "name",
                validators = listOf(Validators.Required()),
            ),
            TextFieldState(
                name = "balance",
                validators = listOf(Validators.MinValue(0, "Amount must be higher than 0"), Validators.Required()),
            ),
            TextFieldState(
                name = "description",
                validators = listOf(Validators.Custom("This field is required") { value -> !(_state.value.currentType == AccountType.CARD && value.toString().isEmpty()) }),
            )
        )
    )
    val transactionFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "amount",
                validators = listOf(
                    Validators.MinValue(0, "Amount must be higher than 0"),
                    Validators.Required(),
                    Validators.Custom("You can't transfer more than you have.")
                        { value -> try { value.toString().toDouble() } catch (ex: java.lang.NumberFormatException) { 0.0 } <= (_state.value.selectedAccount?.balance ?: 0.0) }
                ),
            )
        )
    )

    init { getAccounts() }

    fun onEvent(event: ManageAccountsEvent) {
        when(event) {
            is ManageAccountsEvent.CardSelected -> {
                _state.value = _state.value.copy(
                    selectedAccount = event.account,
                    selectedAccountId = event.account.accountId,
                    currentType = event.account.type
                )
                getTransactions(_state.value.selectedAccountId)
            }
            is ManageAccountsEvent.ToggleActive -> {
                viewModelScope.launch {
                    _state.value.selectedAccount?.let { account ->
                        _state.value = _state.value.copy(
                            selectedAccount = account.copy(active = !account.active)
                        )
                        useCases.add(_state.value.selectedAccount!!)
                        if(account.primary) setPrimary(_state.value.accounts.first { it.accountId != account.accountId }.accountId!!)
                    }
                }
            }
            is ManageAccountsEvent.PrimarySelected -> {
                _state.value.selectedAccount?.let {
                    viewModelScope.launch { setPrimary(it.accountId!!) }
                }
            }
            is ManageAccountsEvent.ToggleAddEdit -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        isAddEditOpen = !_state.value.isAddEditOpen,
                        selectedAccountId = event.account?.accountId
                    )
                    if(event.account == null) {
                        if(!_state.value.isAddEditOpen) delay(450)
                        addEditFormState.fields[0].change("")
                        addEditFormState.fields[1].change("")
                        addEditFormState.fields[2].change("")
                        selectType(AccountType.CARD)
                    } else {
                        addEditFormState.fields[0].change(event.account.name)
                        addEditFormState.fields[1].change(event.account.balance.toInt().toString())
                        addEditFormState.fields[2].change(event.account.description)
                        selectType(event.account.type)
                    }
                }
            }
            is ManageAccountsEvent.TypeChanged -> selectType(event.type)
            is ManageAccountsEvent.SaveAccount -> {
                viewModelScope.launch {
                    if(_state.value.selectedAccount == null) return@launch
                    _state.value = _state.value.copy(
                        selectedAccount = _state.value.selectedAccount?.copy(
                            name = addEditFormState.fields[0].value,
                            balance = addEditFormState.fields[1].value.toDouble(),
                            description = addEditFormState.fields[2].value
                        )
                    )
                    useCases.add(Account(
                        name = _state.value.selectedAccount!!.name,
                        balance = _state.value.selectedAccount!!.balance,
                        active = _state.value.selectedAccount?.active ?: false,
                        accountId = _state.value.selectedAccountId,
                        primary = _state.value.selectedAccount?.primary ?: false,
                        type = _state.value.currentType,
                        description =_state.value.selectedAccount!!.description,
                        deleted = _state.value.selectedAccount?.deleted ?: false
                    ))
                    _state.value.selectedAccount?.let { onEvent(ManageAccountsEvent.CardSelected(it)) }
                    onEvent(ManageAccountsEvent.ToggleAddEdit(null))
                }
            }
            is ManageAccountsEvent.DeleteAccount -> {
                viewModelScope.launch {
                    _state.value.selectedAccount?.let { account ->
                        useCases.delete(account.accountId!!)
                        if(account.primary) setPrimary(_state.value.accounts.first { it.accountId != account.accountId }.accountId!!)
                    }
                }
                onEvent(ManageAccountsEvent.ToggleAddEdit(null))
            }
            is ManageAccountsEvent.ToggleTransaction -> {
                _state.value = _state.value.copy(
                    isTransactionOpen = !_state.value.isTransactionOpen
                )
                if(!_state.value.isTransactionOpen) {
                    transactionFormState.fields[0].change("")
                }
            }
            is ManageAccountsEvent.ConfirmTransaction -> {
                viewModelScope.launch {
                    useCases.addTransaction(_state.value.selectedAccount!!, event.accountTo, transactionFormState.fields[0].value.toDouble())
                }
                onEvent(ManageAccountsEvent.ToggleTransaction)
            }
        }
    }

    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAll()
            .onEach {
                _state.value = _state.value.copy(
                    accounts = it.toMutableList(),
                    selectedAccount = _state.value.selectedAccount ?: it.first()
                )
                getTransactions(_state.value.selectedAccount?.accountId)
            }
            .launchIn(viewModelScope)
    }
    private fun selectType(type: AccountType) {
        _state.value.accountTypeStates.forEach { (value, _) ->
            _state.value.accountTypeStates[value] = mutableStateOf(value.value == type.value)
        }
        _state.value = _state.value.copy(currentType = type)
    }
    private suspend fun setPrimary(id: Int) {
        useCases.setPrimary(id)
        _state.value.selectedAccount = _state.value.selectedAccount?.copy(primary = _state.value.selectedAccount?.accountId == id)
    }

    private var getFinancesJob: Job? = null
    private fun getTransactions(accountId: Int?) {
        if(accountId == null) return
        getFinancesJob?.cancel()
        getFinancesJob = useCases.getTransactions(Pair(-1L, Long.MAX_VALUE), accountId, listOf(FinanceType.TRANSACTION, FinanceType.INCOME))
            .onEach { finance ->
                _state.value = _state.value.copy(
                    results = finance
                )
            }
            .launchIn(viewModelScope)
    }

}