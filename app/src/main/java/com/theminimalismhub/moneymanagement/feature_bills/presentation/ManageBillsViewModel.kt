package com.theminimalismhub.moneymanagement.feature_bills.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageBillsViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf(ManageBillsState())
    val state: State<ManageBillsState> = _state

    fun onEvent(event: ManageBillsEvent) {
        when(event) {
            is ManageBillsEvent.ToggleAddEdit -> {

            }
        }
    }
}