package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

sealed class HomeEvent {
    data class ToggleAddEditCard(val finance: Finance?): HomeEvent()
    data class RangeChanged(val range: Pair<Long, Long>): HomeEvent()
    data class CategoryClicked(val id: Int) : HomeEvent()
    data class ItemTypeSelected(val idx: Int) : HomeEvent()
    data class AccountClicked(val id: Int) : HomeEvent()
}
