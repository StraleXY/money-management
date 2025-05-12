package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 10.dp,
    gapLength: Dp = 5.dp
) {
    val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }
    val dashPx = with(LocalDensity.current) { dashLength.toPx() }
    val gapPx = with(LocalDensity.current) { gapLength.toPx() }
    val radiusPx = with(LocalDensity.current) { (0.dp).toPx() }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(strokeWidth)
    ) {
        drawLine(
            color = color,
            start = Offset(radiusPx, size.height / 2),
            end = Offset(size.width - radiusPx, size.height / 2),
            strokeWidth = strokePx,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)
        )
//        drawCircle(
//            color = color,
//            radius = radiusPx,
//            center = Offset(0f, size.height / 2),
//            style = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f))
//        )
//        drawCircle(
//            color = color,
//            radius = radiusPx,
//            center = Offset(size.width, size.height / 2),
//            style = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f))
//        )
    }
}