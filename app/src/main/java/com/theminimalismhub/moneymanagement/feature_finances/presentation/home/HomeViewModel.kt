package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceService
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    addEditFinanceUseCases: AddEditFinanceUseCases
) : ViewModel() {

    private val _addEditFinanceState = mutableStateOf(AddEditFinanceState())
    val addEditFinanceState: State<AddEditFinanceState> = _addEditFinanceState

    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState

    val addEditService = AddEditFinanceService(
        state = _addEditFinanceState,
        scope = viewModelScope,
        useCases = addEditFinanceUseCases
    )
    init {
        addEditService.getCategories()
    }
    fun onEvent(event: AddEditFinanceEvent) {
        addEditService.onEvent(event)
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ToggleAddEditCard -> {
                _homeState.value = _homeState.value.copy(isAddEditOpen = !_homeState.value.isAddEditOpen)
                if(!_homeState.value.isAddEditOpen) return
                addEditService.onEvent(AddEditFinanceEvent.ToggleAddEditCard(event.finance))
            }
        }
    }



}

