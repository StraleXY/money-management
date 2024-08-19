package com.theminimalismhub.moneymanagement.feature_report.presentation

import com.theminimalismhub.moneymanagement.core.enums.FinanceType

sealed class ReportEvent {
    data class SwitchYear(val direction: Int) : ReportEvent()
    data class ChangeFinanceType(val type: FinanceType) : ReportEvent()
}