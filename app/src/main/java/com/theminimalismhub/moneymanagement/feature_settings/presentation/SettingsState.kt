package com.theminimalismhub.moneymanagement.feature_settings.presentation

data class SettingsState(
    val showLineGraph: Boolean = false,
    val collapseCategories: Boolean = false,
    val filterIncomeByAccount: Boolean = false,
    val filterOutcomeByAccount: Boolean = false
)
