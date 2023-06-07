package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBar
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBarState
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount

@Composable
fun CategoryTotalsOverview(
    totalPerCategory: List<CategoryAmount>,
    categoryBarStates: HashMap<Int, MutableState<CategoryBarState>>,
    currency: String = "RSD",
    onClick: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(red = 0.1f, green = 0.1f, blue = 0.1f),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {
            AnimatedVisibility(
                visible = totalPerCategory.isEmpty(),
                enter = fadeIn(tween(100, 150)),
                exit = fadeOut(tween(100))
            ) { ErrorNoData() }

            AnimatedVisibility(
                visible = totalPerCategory.isNotEmpty(),
                enter = fadeIn(tween(250)),
                exit = fadeOut(tween(250))
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    totalPerCategory.forEach { earnings ->
                        CategoryBar(
                            modifier = Modifier.padding(vertical = 3.dp),
                            categoryInfo = earnings,
                            maxAmount = totalPerCategory.maxOf { it.amount } + totalPerCategory.minOf { it.amount } * 0.01,
                            state = categoryBarStates[earnings.categoryId]!!.value,
                            clicked = onClick,
                            currency = currency
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}