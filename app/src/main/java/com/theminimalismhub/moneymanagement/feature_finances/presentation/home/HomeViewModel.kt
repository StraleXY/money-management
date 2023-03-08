package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    addEditFinanceUseCases: AddEditFinanceUseCases
) : ViewModel() {

    val addEditService = AddEditFinanceService(viewModelScope, addEditFinanceUseCases)
    fun onEvent(event: AddEditFinanceEvent) { addEditService.onEvent(event) }


    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        getFinances()
    }
    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ToggleAddEditCard -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
                if(!_state.value.isAddEditOpen) return
                addEditService.onEvent(AddEditFinanceEvent.ToggleAddEditCard(event.finance))
            }
        }
    }

    private var getFinances: Job? = null
    private fun getFinances() {
        getFinances?.cancel()
        getFinances = useCases.getFinances()
            .onEach { finance ->
                _state.value = _state.value.copy(
                    results = finance
                )
            }
            .launchIn(viewModelScope)
    }
}

