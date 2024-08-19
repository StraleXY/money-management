package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.core.composables.AutoResizeText
import com.theminimalismhub.moneymanagement.core.composables.FontSizeRange
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.ui.theme.economica
import kotlin.math.abs

data class CategoryAmount(
    val categoryId: Int,
    val accountId: Int,
    val name: String,
    val color: Int,
    val amount: Double,
    val count: Int
)

enum class CategoryBarState {
    SELECTED,
    NEUTRAL,
    DESELECTED
}

@Composable
fun CategoryBar(
    modifier: Modifier = Modifier,
    categoryInfo: CategoryAmount,
    maxAmount: Double,
    state: CategoryBarState = CategoryBarState.NEUTRAL,
    clicked: (Int) -> Unit = {},
    currency: String = "RSD",
    showCount: Boolean
) {
    fun normalize(x: Double) : Double {
        return Math.max(Math.min(Math.log(x + 0.65) + 0.5, 1.0), 0.0)
    }
    fun calc(x: Double) : Float {
        val fraction = normalize((x / maxAmount.coerceAtLeast(1.0)))
        return (fraction * 0.7f).toFloat().coerceAtLeast(0.05f)
    }
    // val animatedScale = animateFloatAsState(targetValue = if(state == CategoryBarState.DESELECTED) 0.95f else 1f)
    val animatedWidth = remember { Animatable(0.045f) }
    val animatedAlpha = animateFloatAsState(targetValue = if(animatedWidth.value == 0.045f) 0f else if(state == CategoryBarState.DESELECTED) 0.5f else 1f, tween(250))
    val animatedColor by animateColorAsState(targetValue = Colorer.getAdjustedDarkColor(categoryInfo.color), tween(200))

    LaunchedEffect(categoryInfo) {
        val width = calc(categoryInfo.amount)
        val prevWidth = animatedWidth.value
        val duration: Int = (abs(width - animatedWidth.value) * 1400).toInt().coerceAtLeast(400).coerceAtMost(1200)
        animatedWidth.animateTo(
            targetValue = calc(categoryInfo.amount),
            animationSpec = keyframes {
                durationMillis = duration
                prevWidth at 0 with Easing { OvershootInterpolator(1f).getInterpolation(it) }
                width at duration with Easing { OvershootInterpolator(1f).getInterpolation(it) }
            }
        )
    }

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
                .background(animatedColor),
            content = {}
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            AutoResizeText(
                text = categoryInfo.name + if(showCount) " [${categoryInfo.count}]" else "",
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
                text = "${Currencier.formatAmount(categoryInfo.amount)} $currency",
                color = animatedColor,
                style = MaterialTheme.typography.body1.copy(
                    fontFamily = economica,
                    fontSize = 20.sp
                )
            )
        }
    }
}

