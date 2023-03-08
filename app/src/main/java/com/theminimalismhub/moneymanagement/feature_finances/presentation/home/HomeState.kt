package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class HomeState(
    val isAddEditOpen: Boolean = false,
    val categories: List<Category> = emptyList(),
    val currentFinanceId: Int? = null,
    val selectedCategoryId: Int? = null,
    val currentType: FinanceType = FinanceType.OUTCOME,
    val categoryStates: HashMap<Int, MutableState<Boolean>> = HashMap(),
    val timestamp: Long = System.currentTimeMillis()
)
