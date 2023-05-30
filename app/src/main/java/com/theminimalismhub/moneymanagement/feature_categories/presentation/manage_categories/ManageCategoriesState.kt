package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import androidx.compose.ui.graphics.Color
import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HSVColor
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class ManageCategoriesState(
    val outcomeCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
    val isAddEditOpen: Boolean = false,
    val currentCategory: Category? = null,
    val currentId: Int? = null,
    val currentType: FinanceType = FinanceType.OUTCOME,
    val currentName: String = "",
    val currentColor: HSVColor = HSVColor.from(Color.White),
    val currentTrackable: Boolean = true
)
