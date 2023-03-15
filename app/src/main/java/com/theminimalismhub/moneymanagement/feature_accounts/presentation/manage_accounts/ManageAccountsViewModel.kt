package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.ManageAccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAccountsViewModel @Inject constructor(
    private val useCases: ManageAccountsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ManageAccountsState())
    val state: State<ManageAccountsState> = _state

    val formState = FormState(
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
                validators = if(_state.value.currentType == AccountType.CASH) listOf(Validators.Required()) else emptyList(),
            )
        )
    )

    init { getAccounts() }

    fun onEvent(event: ManageAccountsEvent) {
        when(event) {
            is ManageAccountsEvent.CardSelected -> {
                if(event.idx >= _state.value.accounts.size) return
                _state.value = _state.value.copy(
                    selectedAccount = _state.value.accounts[event.idx],
                    selectedAccountId = _state.value.accounts[event.idx].accountId,
                    currentType = _state.value.accounts[event.idx].type
                )
            }
            is ManageAccountsEvent.ToggleActive -> {
                viewModelScope.launch {
                    _state.value.selectedAccount?.let {
                        _state.value = _state.value.copy(
                            selectedAccount = it.copy(active = !it.active)
                        )
                        useCases.add(_state.value.selectedAccount!!)
                    }
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
                        formState.fields[0].change("")
                        formState.fields[1].change("")
                        formState.fields[2].change("")
                        selectType(AccountType.CARD)
                    } else {
                        formState.fields[0].change(event.account.name)
                        formState.fields[1].change(event.account.balance.toInt().toString())
                        formState.fields[2].change(event.account.description)
                        selectType(event.account.type)
                    }
                }
            }
            is ManageAccountsEvent.TypeChanged -> selectType(event.type)
            ManageAccountsEvent.SaveAccount -> {
                viewModelScope.launch {
                    useCases.add(Account(
                        name = formState.fields[0].value,
                        balance = formState.fields[1].value.toDouble(),
                        active = _state.value.selectedAccount?.active ?: false,
                        accountId = _state.value.selectedAccountId,
                        primary = _state.value.selectedAccount?.primary ?: false,
                        type = _state.value.currentType,
                        description = formState.fields[2].value
                    ))
                    _state.value.selectedAccount?.let { onEvent(ManageAccountsEvent.CardSelected(_state.value.accounts.indexOf(it))) }
                    onEvent(ManageAccountsEvent.ToggleAddEdit(null))
                }
            }
        }
    }

    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAccounts()
            .onEach {
                _state.value = _state.value.copy(
                    accounts = it,
                    selectedAccount = _state.value.selectedAccount ?: it.first()
                )
            }
            .launchIn(viewModelScope)
    }
    private fun selectType(type: AccountType) {
        _state.value.accountTypeStates.forEach { (value, _) ->
            _state.value.accountTypeStates[value] = mutableStateOf(value.value == type.value)
        }
        _state.value = _state.value.copy(currentType = type)
    }

}