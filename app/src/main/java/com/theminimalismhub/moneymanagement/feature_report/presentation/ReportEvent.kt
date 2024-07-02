package com.theminimalismhub.moneymanagement.feature_report.presentation

sealed class ReportEvent {
    data class SwitchYear(val direction: Int) : ReportEvent()
}