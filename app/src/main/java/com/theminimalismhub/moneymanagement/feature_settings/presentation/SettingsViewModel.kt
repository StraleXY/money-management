package com.theminimalismhub.moneymanagement.feature_settings.presentation

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

    val formState = FormState(
        fields = listOf(
            TextFieldState(
                name = "limit",
                validators = listOf(Validators.Required()),
            )
        )
    )

    init {
        formState.fields[0].change(preferences.getSimpleLimit().toInt().toString())
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.OnDailyLimitChanged -> {
                if(event.value.isNotEmpty()) preferences.setSimpleLimit(event.value.toFloat())
                else preferences.setSimpleLimit(1000f)
            }
        }
    }
}