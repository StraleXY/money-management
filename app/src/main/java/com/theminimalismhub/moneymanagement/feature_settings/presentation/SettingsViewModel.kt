package com.theminimalismhub.moneymanagement.feature_settings.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val preferences: Preferences
) : ViewModel() {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    val formState = FormState(
        fields = listOf(
            TextFieldState(
                name = "limit",
                validators = listOf(Validators.Required()),
            ),
            TextFieldState(
                name = "currency",
                validators = listOf(Validators.Required()),
            )
        )
    )

    init {
        formState.fields[0].change(preferences.getSimpleLimit().toInt().toString())
        formState.fields[1].change(preferences.getCurrency())
        _state.value = _state.value.copy(
            showLineGraph = preferences.getShowLineGraph(),
            collapseCategories = preferences.getCollapseCategories(),
            filterIncomeByAccount = preferences.getFilterIncomeByAccount(),
            filterOutcomeByAccount = preferences.getFilterOutcomeByAccount()
        )
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.OnDailyLimitChanged -> {
                if(event.value.isNotEmpty()) preferences.setSimpleLimit(event.value.toFloat())
                else preferences.setSimpleLimit(1000f)
            }
            is SettingsEvent.OnCurrencyChanged -> {
                preferences.setCurrency(event.value)
            }
            is SettingsEvent.ToggleShowLineGraph -> {
                _state.value = _state.value.copy(showLineGraph = !_state.value.showLineGraph)
                preferences.setShowLineGraph(_state.value.showLineGraph)
            }
            is SettingsEvent.ToggleCollapseCategories -> {
                _state.value = _state.value.copy(collapseCategories = !_state.value.collapseCategories)
                preferences.setCollapseCategories(_state.value.collapseCategories)
            }
            is SettingsEvent.ToggleFilterIncomeByAccount -> {
                _state.value = _state.value.copy(filterIncomeByAccount = !_state.value.filterIncomeByAccount)
                preferences.setFilterIncomeByAccount(_state.value.filterIncomeByAccount)
            }
            is SettingsEvent.ToggleFilterOutcomeByAccount -> {
                _state.value = _state.value.copy(filterOutcomeByAccount = !_state.value.filterOutcomeByAccount)
                preferences.setFilterOutcomeByAccount(_state.value.filterOutcomeByAccount)
            }
        }
    }
}