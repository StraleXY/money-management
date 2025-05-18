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
            }
            is ManageFundsEvent.SaveFund -> {
                viewModelScope.launch {
                    val id = useCases.addFund(Fund(
                        item = FundItem(
                            name = "Test Reservation",
                            type = FundType.RESERVATION,
                            amount = Random(System.currentTimeMillis()).nextDouble(5000.0, 50000.0)
                        ),
                        categories = listOf(_state.value.categories.random()),
                        accounts = listOf(_state.value.accounts.random())
                    ))
                    Log.d("Fund", "Inserted a fund with id: $id")
                }
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