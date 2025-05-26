package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ManageCategoriesEvent
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeEvent
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

sealed class AddEditFinanceEvent {
    data class ToggleAddEditCard(val finance: Finance?, val recommended: RecommendedFinance? = null): AddEditFinanceEvent()
    data class AccountSelected(val id: Int): AddEditFinanceEvent()
    object ToggleType : AddEditFinanceEvent()
    data class CategorySelected(val id: Int): AddEditFinanceEvent()
    data class DateChanged(val timestamp: Long): AddEditFinanceEvent()
    object AddFinance: AddEditFinanceEvent()
    object DeleteFinance: AddEditFinanceEvent()
    object TrackableToggled: AddEditFinanceEvent()
    data class SelectBudget(val budget: Fund?) : AddEditFinanceEvent()
}
