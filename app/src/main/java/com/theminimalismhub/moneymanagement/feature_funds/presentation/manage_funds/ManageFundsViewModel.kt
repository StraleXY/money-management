package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class ManageFundsViewModel @Inject constructor(
    val useCases: ManageFundsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ManageFundsState())
    val state: State<ManageFundsState> = _state

    init { init() }

    fun init() {
        getCategories()
        getAccounts()
        getFunds()
    }

    fun onEvent(event: ManageFundsEvent) {
        when (event) {
            is ManageFundsEvent.SaveFund -> {
                viewModelScope.launch {
                    val id = useCases.addFund(Fund(
                        item = FundItem(
                            name = "New Glasses",
                            type = FundType.RESERVATION,
                            amount = 35000.0
                        ),
                        categories = _state.value.categories.filter { it.name == "Odeća" },
                        accounts = _state.value.accounts.filter { it.primary }
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