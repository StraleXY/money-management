package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun onEvent(event: HomeEvents) {
        when(event) {
            is HomeEvents.ToggleAddEditCard -> {
                _state.value = _state.value.copy(isAddEditOpen = !_state.value.isAddEditOpen)
            }
        }
    }

}