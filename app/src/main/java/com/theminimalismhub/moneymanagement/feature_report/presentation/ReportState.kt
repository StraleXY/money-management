package com.theminimalismhub.moneymanagement.feature_report.presentation

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBarState

data class ReportState(
    val selectedType: FinanceType = FinanceType.OUTCOME,
    val year: Int = 2023,
    val dateRange: Pair<Long, Long> = Pair(System.currentTimeMillis(), System.currentTimeMillis()),
    val limit: Double = 0.0,
    val currency: String = "",
    val results: List<Finance> = emptyList(),
    val total: Double = 0.0,
    val totalPerCategory: List<CategoryAmount> = emptyList(),
    val categoryBarStates: HashMap<Int, MutableState<CategoryBarState>> = HashMap(),
)
