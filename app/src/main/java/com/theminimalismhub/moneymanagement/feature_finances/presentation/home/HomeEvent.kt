package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.material.Colors
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance

sealed class HomeEvent {
    data class Init(val colors: Colors): HomeEvent()
    data class ToggleAddEditCard(val finance: Finance?, val recommended: RecommendedFinance? = null): HomeEvent()
    data class RangeChanged(val range: Pair<Long, Long>, val isToday: Boolean): HomeEvent()
    data class CategoryClicked(val id: Int) : HomeEvent()
    data class ItemTypeSelected(val idx: Int) : HomeEvent()
    data class DisplayTypeChanged(val types: List<FinanceType>) : HomeEvent()
    data class DisplayTrackedChanged(val tracked: List<Boolean>) : HomeEvent()
    data class AccountClicked(val id: Int) : HomeEvent()
    object ToggleShowLineGraph : HomeEvent()
    data class DeleteRecommendedFinance(val id: Int) : HomeEvent()
    data class PayRecommendedFinance(val recommended: RecommendedFinance) : HomeEvent()
}
