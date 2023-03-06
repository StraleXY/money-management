package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HSVColor
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.ManageCategoriesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val useCases: ManageCategoriesUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ManageCategoriesState())
    val state: State<ManageCategoriesState> = _state
    private var typeLabelJob: Job? = null
    private var nameStore by mutableStateOf("")

    init {
        getCategories()
    }

    fun onEvent(event: ManageCategoriesEvent) {
        when(event){
            is ManageCategoriesEvent.ToggleAddEditCard -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        isAddEditOpen = !_state.value.isAddEditOpen
                    )
                    if(!_state.value.isAddEditOpen) delay(350)
                    _state.value = _state.value.copy(
                        currentId = if(event.category == null) null else event.category.categoryId,
                        currentType = if(event.category == null) FinanceType.OUTCOME else event.category.type,
                        currentName = if(event.category == null) "" else event.category.name,
                        currentColor = HSVColor.from(if(event.category == null) Color.White else Color(event.category.color))
                    )
                }
                nameStore = if(event.category == null) "" else event.category.name
            }
            is ManageCategoriesEvent.ToggleType -> {
                typeLabelJob?.let {
                    _state.value = _state.value.copy(currentName = nameStore)
                    it.cancel()
                }
                typeLabelJob = viewModelScope.launch {
                    nameStore = _state.value.currentName
                    _state.value = _state.value.copy(
                        currentType = FinanceType[(_state.value.currentType.value + 1) % 2]!!,
                        currentName = FinanceType[(_state.value.currentType.value + 1) % 2].toString()
                    )
                    delay(500)
                    _state.value = _state.value.copy(currentName = nameStore)
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
            is ManageCategoriesEvent.SaveCategory -> {
                viewModelScope.launch {
                    useCases.add(
                        Category(
                            categoryId = state.value.currentId,
                            name = state.value.currentName,
                            color = state.value.currentColor.toColor().toArgb(),
                            isDeleted = false,
                            type = state.value.currentType
                        )
                    )
                }
                onEvent(ManageCategoriesEvent.ToggleAddEditCard(null))
            }
            is ManageCategoriesEvent.DeleteCategory -> {
                viewModelScope.launch {
                    useCases.delete(_state.value.currentId!!)
                    onEvent(ManageCategoriesEvent.ToggleAddEditCard(null))
                }
            }
        }
    }

    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.get()
            .onEach {
                _state.value = _state.value.copy(
                    incomeCategories = it.filter { it.type == FinanceType.INCOME },
                    outcomeCategories = it.filter { it.type == FinanceType.OUTCOME }
                )
            }
            .launchIn(viewModelScope)
    }

}