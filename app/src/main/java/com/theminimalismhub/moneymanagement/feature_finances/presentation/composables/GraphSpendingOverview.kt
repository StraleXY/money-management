package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData

@Composable
fun GraphSpendingOverview(
    modifier: Modifier = Modifier,
    earningsPerTimePeriod: List<GraphEntry>,
    maxEarnings: Double,
    limit: Double
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if(MaterialTheme.colors.isLight) Color(ColorUtils.blendARGB(MaterialTheme.colors.surface.toArgb(), Color.Black.toArgb(), 0.02f))
                else MaterialTheme.colors.surface.copy(1f, 0.08f, 0.08f, 0.08f)),
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            AnimatedVisibility(
                visible = earningsPerTimePeriod.sumOf { it.value } == 0.0,
                enter = fadeIn(tween(100, 150)),
                exit = fadeOut(tween(100))
            ) { ErrorNoData(modifier = Modifier.padding(vertical = 20.dp)) }

            AnimatedVisibility(
                visible = earningsPerTimePeriod.sumOf { it.value } > 0,
                enter = fadeIn(tween(250)),
                exit = fadeOut(tween(250))
            ) {
                Graph(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    totalHeight = 160.dp,
                    earnings = earningsPerTimePeriod,
                    maxEarnings = maxEarnings,
                    limit = limit
                )
            }
        }
    }
}