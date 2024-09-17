package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBar
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBarState
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount

@Composable
fun CategoryTotalsOverview(
    totalPerCategory: List<CategoryAmount>,
    categoryBarStates: HashMap<Int, MutableState<CategoryBarState>>,
    currency: String = "RSD",
    collapsable: Boolean = false,
    collapsedCount: Int = 5,
    showCount: Boolean = false,
    onClick: (Int) -> Unit
) {
    var showAmount by remember { mutableStateOf(collapsedCount) }
    fun toggleShowAmount() {
        showAmount = if(showAmount < totalPerCategory.size) totalPerCategory.size else collapsedCount
    }
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        backgroundColor =
            if(MaterialTheme.colors.isLight) Color(ColorUtils.blendARGB(MaterialTheme.colors.surface.toArgb(), Color.Black.toArgb(), 0.03f))
            else MaterialTheme.colors.surface.copy(1f, 0.1f, 0.1f, 0.1f),
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
                    totalPerCategory.take(if(collapsable) showAmount else totalPerCategory.size).forEach { earnings ->
                        CategoryBar(
                            modifier = Modifier.padding(vertical = 3.dp),
                            categoryInfo = earnings,
                            maxAmount = totalPerCategory.maxOf { it.amount } + totalPerCategory.minOf { it.amount } * 0.01,
                            state = categoryBarStates[earnings.categoryId]!!.value,
                            clicked = onClick,
                            currency = currency,
                            showCount = showCount
                        )
                    }
                }
            }
        }
    }
    if (collapsable && totalPerCategory.size > collapsedCount) Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            modifier = Modifier.padding(end = 24.dp),
            onClick = { toggleShowAmount() }
        ) {
            Text(
                text = if(showAmount < totalPerCategory.size) "SHOW ALL" else "COLLAPSE",
                style = MaterialTheme.typography.button
            )
        }
    }
    else Spacer(modifier = Modifier.height(24.dp))
}