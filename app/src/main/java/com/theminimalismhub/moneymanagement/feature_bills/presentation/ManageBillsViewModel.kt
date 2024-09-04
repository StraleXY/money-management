package com.theminimalismhub.moneymanagement.feature_bills.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddEditBillUseCases
import com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill.AddEditBillService
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ManageBillsViewModel @Inject constructor(
    private val useCases: AddEditBillUseCases,
    preferences: Preferences
) : ViewModel() {

    val addEditBillVM: AddEditBillService = AddEditBillService(viewModelScope, useCases, preferences) {
        onEvent(ManageBillsEvent.ToggleAddEdit(null))
        getBills()
    }

    private val _state = mutableStateOf(ManageBillsState())
    val state: State<ManageBillsState> = _state

    val paymentFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "amount",
                validators = listOf(Validators.Required()),
            )
        )
    )

    init {
        _state.value = _state.value.copy(currency = preferences.getCurrency())
        getBills()
        getAccounts()
    }

    fun onEvent(event: ManageBillsEvent) {
        when(event) {
            is ManageBillsEvent.ToggleAddEdit -> {
                _state.value = _state.value.copy(
                    isAddEditOpen = !_state.value.isAddEditOpen
                )
                if(event.bill == null) viewModelScope.launch {
                    delay(300)
                    addEditBillVM.clear()
                }
                else addEditBillVM.initBill(event.bill)
            }
            is ManageBillsEvent.TogglePayBill -> {
                _state.value = _state.value.copy(billToPay = event.bill)
                event.bill?.let {
                    paymentFormState.fields[0].change(it.bill.amount.toInt().toString())
                    onEvent(ManageBillsEvent.PaymentAccountSelected(it.bill.billAccountId))
                }
            }
            is ManageBillsEvent.PayBill -> {
                _state.value.billToPay?.let { bill ->
                    viewModelScope.launch {
                        useCases.pay(FinanceItem(
                            name = bill.bill.name,
                            amount = (paymentFormState.fields[0].value).toDouble(),
                            timestamp = System.currentTimeMillis(),
                            type = FinanceType.OUTCOME,
                            financeCategoryId = bill.category.categoryId!!,
                            financeAccountId = _state.value.paymentAccountId!!,
                            trackable = bill.category.trackable
                        ))
                        useCases.updateAccountBalance(-(paymentFormState.fields[0].value).toDouble(), _state.value.paymentAccountId!!)
                        useCases.add(bill.getPayedBill())
                        onEvent(ManageBillsEvent.TogglePayBill(null))
                    }
                }
            }
            is ManageBillsEvent.PaymentAccountSelected -> {
                _state.value.accountStates.forEach { (id, _) ->
                    _state.value.accountStates[id]?.value = id == event.accountId
                }
                _state.value = _state.value.copy(paymentAccountId = event.accountId)
            }
        }
    }

    private var getJob: Job? = null
    fun getBills() {
        getJob?.cancel()
        getJob = useCases.get()
            .onEach {
                _state.value = _state.value.copy(bills = it)
            }
            .launchIn(viewModelScope)
    }

    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAccounts()
            .onEach { accounts ->
                _state.value = _state.value.copy(
                    accounts = accounts,
                    paymentAccountId = accounts.find { it.primary }?.accountId
                )
                accounts.forEach { account ->
                    account.accountId?.let { id -> _state.value.accountStates[id] = mutableStateOf(account.accountId == _state.value.paymentAccountId) }
                }
            }
            .launchIn(viewModelScope)
    }
}