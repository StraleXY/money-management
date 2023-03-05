package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HSVColor
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

sealed class ManageCategoriesEvent {
    data class  ToggleAddEditCard(val category: Category?): ManageCategoriesEvent()
    data class ColorChanged(val value: HSVColor): ManageCategoriesEvent()
}
