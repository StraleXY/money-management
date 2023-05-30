package com.theminimalismhub.moneymanagement.feature_settings.presentation

sealed class SettingsEvent {
    data class OnDailyLimitChanged(val value: String) : SettingsEvent()
}
