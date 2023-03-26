package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HSVColor
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

sealed class ManageCategoriesEvent {
    data class  ToggleAddEditCard(val category: Category?): ManageCategoriesEvent()
    object ToggleType: ManageCategoriesEvent()
    data class EnteredName(val value: String): ManageCategoriesEvent()
    data class ColorChanged(val value: HSVColor): ManageCategoriesEvent()
    object SaveCategory: ManageCategoriesEvent()
    object DeleteCategory: ManageCategoriesEvent()
    object TrackableToggled: ManageCategoriesEvent()
}
