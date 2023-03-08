package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

sealed class HomeEvent {
    data class ToggleAddEditCard(val finance: Finance?): HomeEvent()
    object ToggleType : HomeEvent()
    data class CategorySelected(val id: Int): HomeEvent()
    data class DateChanged(val timestamp: Long): HomeEvent()
    object AddFinance: HomeEvent()

}
