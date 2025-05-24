package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases.ManageFundsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ManageFundsViewModel @Inject constructor(
    val useCases: ManageFundsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ManageFundsState())
    val state: State<ManageFundsState> = _state

    val addEditFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "name",
                validators = listOf(Validators.Required()),
            ),
            TextFieldState(
                name = "amount",
                validators = listOf(Validators.Required(), Validators.MinValue(0, "Amount must be higher than 0")),
            )
        )
    )

    init { init() }

    fun init() {
        getCategories()
        getAccounts()
        getFunds()
    }

    fun onEvent(event: ManageFundsEvent) {
        when (event) {
            is ManageFundsEvent.ToggleAddEdit -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
                if(event.item == null) {
                    _state.value = _state.value.copy(
                        sFundItem = null,
                        sFundType = FundType.BUDGET,
                        sRecurring = null,
                        sAccounts = emptyList(),
                        sCategories = emptyList()
                    )
                    addEditFormState.fields[0].change("")
                    addEditFormState.fields[1].change("")
                }
                else {
                    _state.value = _state.value.copy(
                        sFundItem = event.item.item,
                        sFundType = event.item.item.type,
                        sRecurring = event.item.item.recurringType,
                        sAccounts = _state.value.accounts.filter { event.item.accounts.map { ia -> ia.accountId }.contains(it.accountId) },
                        sCategories = _state.value.categories.filter { event.item.categories.map { ic -> ic.categoryId }.contains(it.categoryId) }
                    )
                    addEditFormState.fields[0].change(event.item.item.name)
                    addEditFormState.fields[1].change(event.item.item.amount.toInt().toString()) // TODO Double to Int
                }
            }
            is ManageFundsEvent.SaveFund -> {
                viewModelScope.launch {
                    useCases.addFund(
                        Fund(
                            item = FundItem(
                                fundId = _state.value.sFundItem?.fundId,
                                name = addEditFormState.fields[0].value,
                                amount = addEditFormState.fields[1].value.toDoubleOrNull() ?: 0.0,
                                type = _state.value.sFundType,
                                recurringType = _state.value.sRecurring,
                                startDate = _state.value.sFundItem?.startDate ?: System.currentTimeMillis()
                            ),
                            categories = _state.value.sCategories,
                            accounts = _state.value.sAccounts
                        )
                    )
                    onEvent(ManageFundsEvent.ToggleAddEdit())
                }
            }
            is ManageFundsEvent.SelectFundType -> {
                _state.value = _state.value.copy(sFundType = event.type)
                onEvent(ManageFundsEvent.SelectAccounts())
                onEvent(ManageFundsEvent.SelectCategories())
                onEvent(ManageFundsEvent.SelectRecurring())
            }
            is ManageFundsEvent.SelectAccounts -> {
                _state.value = _state.value.copy(sAccounts = _state.value.accounts.filter { event.ids.contains(it.accountId) })
            }
            is ManageFundsEvent.SelectCategories -> {
                _state.value = _state.value.copy(sCategories = _state.value.categories.filter { event.ids.contains(it.categoryId) })
            }
            is ManageFundsEvent.SelectRecurring -> {
                _state.value = _state.value.copy(sRecurring = event.recurring)
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

    // Accounts
    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAccounts()
            .onEach { _state.value = _state.value.copy(accounts = it) }
            .launchIn(viewModelScope)
    }

    // Get Funds
    private var getFundsJob: Job? = null
    private fun getFunds() {
        getFundsJob?.cancel()
        getFundsJob = useCases.getFunds()
            .onEach {
                _state.value = _state.value.copy(funds = it)
                it.forEach {
                    Log.d("Fund", it.toString())
                }
            }
            .launchIn(viewModelScope)
    }
}