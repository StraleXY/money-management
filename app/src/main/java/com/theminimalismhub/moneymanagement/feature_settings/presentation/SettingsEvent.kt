package com.theminimalismhub.moneymanagement.feature_settings.presentation

sealed class SettingsEvent {
    data class OnDailyLimitChanged(val value: String) : SettingsEvent()
    data class OnCurrencyChanged(val value: String) : SettingsEvent()
    object ToggleShowLineGraph : SettingsEvent()
    object ToggleCollapseCategories : SettingsEvent()
    object ToggleFilterIncomeByAccount : SettingsEvent()
    object ToggleFilterOutcomeByAccount : SettingsEvent()
}
