package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases.ManageFundsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    }


    // Get Categories
    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.getCategories()
            .onEach { _state.value = _state.value.copy(categories = it) }
            .launchIn(viewModelScope)
    }
}