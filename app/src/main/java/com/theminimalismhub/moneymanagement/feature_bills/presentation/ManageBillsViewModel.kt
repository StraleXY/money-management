package com.theminimalismhub.moneymanagement.feature_bills.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddEditBillUseCases
import com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill.AddEditBillService
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ManageBillsViewModel @Inject constructor(
    private val addEditUseCases: AddEditBillUseCases,
    private val preferences: Preferences
) : ViewModel() {

    val addEditBillVM: AddEditBillService = AddEditBillService(viewModelScope, addEditUseCases, preferences) {
        onEvent(ManageBillsEvent.ToggleAddEdit(null))
        getBills()
    }

    private val _state = mutableStateOf(ManageBillsState())
    val state: State<ManageBillsState> = _state

    init {
        getBills()
    }

    fun onEvent(event: ManageBillsEvent) {
        when(event) {
            is ManageBillsEvent.ToggleAddEdit -> {
                _state.value = _state.value.copy(
                    isAddEditOpen = !_state.value.isAddEditOpen
                )
            }
        }
    }

    private var getJob: Job? = null
    fun getBills() {
        getJob?.cancel()
        getJob = addEditUseCases.get()
            .onEach {
                _state.value = _state.value.copy(bills = it)
            }
            .launchIn(viewModelScope)
    }
}