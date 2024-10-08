package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.material.Colors
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

sealed class HomeEvent {
    data class Init(val colors: Colors): HomeEvent()
    data class ToggleAddEditCard(val finance: Finance?): HomeEvent()
    data class RangeChanged(val range: Pair<Long, Long>, val isToday: Boolean): HomeEvent()
    data class CategoryClicked(val id: Int) : HomeEvent()
    data class ItemTypeSelected(val idx: Int) : HomeEvent()
    data class AccountClicked(val id: Int) : HomeEvent()
    object ToggleShowLineGraph : HomeEvent()
}
