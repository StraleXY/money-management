package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.composables.AutoResizeText
import com.theminimalismhub.moneymanagement.core.composables.FontSizeRange
import com.theminimalismhub.moneymanagement.ui.theme.economica

data class CategoryEarnings(
    val categoryId: Int,
    val name: String,
    val color: Int,
    val amount: Double
)

enum class CategoryBarState {
    SELECTED,
    NEUTRAL,
    DESELECTED
}

@Composable
fun CategoryBar(
    modifier: Modifier = Modifier,
    categoryInfo: CategoryEarnings,
    maxAmount: Double,
    state: CategoryBarState = CategoryBarState.NEUTRAL,
    clicked: (Int) -> Unit = {},
    currency: String = "RSD"
) {
    fun normalize(x: Double) : Double {
        return Math.max(Math.min(Math.log(x + 0.65) + 0.5, 1.0), 0.0)
    }
    fun calc(x: Double) : Float {
        val fraction = normalize((x / maxAmount.coerceAtLeast(1.0)))
        return (fraction * 0.65f).toFloat().coerceAtLeast(0.05f)
    }
    val animatedScale = animateFloatAsState(targetValue = if(state == CategoryBarState.DESELECTED) 0.95f else 1f)
    val animatedAlpha = animateFloatAsState(targetValue = if(state == CategoryBarState.DESELECTED) 0.6f else 1f)
    val animatedWidth = animateFloatAsState(
        targetValue = calc(categoryInfo.amount),
        animationSpec = keyframes {
            durationMillis = 1000
            0.05f at 0 with FastOutSlowInEasing
            calc(categoryInfo.amount) at 1000
        },
        visibilityThreshold = 0f
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(animatedAlpha.value)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                clicked(categoryInfo.categoryId)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedWidth.value)
                .height(42.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(categoryInfo.color)),
            content = {}
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            AutoResizeText(
                text = categoryInfo.name,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSizeRange = FontSizeRange(
                    min = 12.sp,
                    max = 16.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = "${categoryInfo.amount.toInt()} $currency",
                color = Color(categoryInfo.color),
                style = MaterialTheme.typography.body1
                    .copy(fontFamily = economica)
            )
        }
    }
}

