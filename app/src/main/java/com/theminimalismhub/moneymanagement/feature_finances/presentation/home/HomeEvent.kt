package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

sealed class HomeEvent {
    data class ToggleAddEditCard(val finance: Finance?): HomeEvent()
}