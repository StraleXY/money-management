package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBar
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBarState
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings

@Composable
fun CategoryTotalsOverview(
    totalPerCategory: List<CategoryEarnings>,
    categoryBarStates: HashMap<Int, MutableState<CategoryBarState>>,
    onClick: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if(totalPerCategory.isEmpty()) ErrorNoData()
            totalPerCategory.forEach { earnings ->
                CategoryBar(
                    modifier = Modifier.padding(vertical = 3.dp),
                    categoryInfo = earnings,
                    maxAmount = totalPerCategory.maxOf { it.amount } + totalPerCategory.minOf { it.amount } * 0.01,
                    state = categoryBarStates[earnings.categoryId]!!.value,
                    clicked = onClick
                )
            }
        }
    }
}