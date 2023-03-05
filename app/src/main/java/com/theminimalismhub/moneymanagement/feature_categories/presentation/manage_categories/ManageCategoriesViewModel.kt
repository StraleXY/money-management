package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf(ManageCategoriesState())
    val state: State<ManageCategoriesState> = _state

    fun onEvent(event: ManageCategoriesEvent) {
        when(event){
            is ManageCategoriesEvent.ToggleAddEditCard -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        isAddEditOpen = !_state.value.isAddEditOpen
                    )
                }
            }
            is ManageCategoriesEvent.EnteredName -> {
                _state.value = _state.value.copy(
                    currentName = event.value
                )
            }
            is ManageCategoriesEvent.ColorChanged -> {
                _state.value = _state.value.copy(
                    currentColor = event.value
                )
            }
        }
    }

}