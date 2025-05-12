package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CouponContainer(
    modifier: Modifier = Modifier,
    segmentsCount: Int = 6,
    segmentSize: Dp = 14.dp,
    strokeWidth: Dp = 2.dp,
    surfaceColor: Color = MaterialTheme.colors.surface,
    strokeColor: Color = MaterialTheme.colors.surface,
    backgroundColor: Color = MaterialTheme.colors.background,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(segmentSize * 1.5f * segmentsCount + (segmentSize / 2))
        ) {

            val widthPx = this.size.width
            val heightPx = this.size.height
            val halfStrokePx = strokeWidth.toPx() / 2

            drawRect(
                color = surfaceColor,
                topLeft = Offset(halfStrokePx, halfStrokePx),
                size = Size(widthPx - halfStrokePx * 2, heightPx - halfStrokePx * 2)
            )

            // Start Side
            drawLine(
                color = strokeColor,
                start = Offset(0f, 0f),
                end = Offset(0f, (segmentSize / 2).toPx()),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            for (i in 0 until segmentsCount) {
                val segmentStartY = i * (segmentSize * 1.5f).toPx() + (segmentSize / 2).toPx()
                drawArc(
                    color = strokeColor,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(-segmentSize.toPx() / 2, segmentStartY),
                    size = Size(segmentSize.toPx(), segmentSize.toPx()),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = backgroundColor,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(-segmentSize.toPx() / 2 + halfStrokePx, segmentStartY + halfStrokePx),
                    size = Size(segmentSize.toPx() - halfStrokePx * 2, segmentSize.toPx() - halfStrokePx * 2),
                    style = Fill
                )
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, segmentStartY + segmentSize.toPx() + 1f),
                    end = Offset(0f, segmentStartY + (segmentSize * 1.5f).toPx() - 1f),
                    strokeWidth = strokeWidth.toPx(),
                    cap = StrokeCap.Butt
                )
            }

            // End Side
            drawLine(
                color = strokeColor,
                start = Offset(widthPx, 0f),
                end = Offset(widthPx, (segmentSize / 2).toPx()),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            for (i in 0 until segmentsCount) {
                val segmentStartY = i * (segmentSize * 1.5f).toPx() + (segmentSize / 2).toPx()
                drawArc(
                    color = strokeColor,
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(widthPx - segmentSize.toPx() / 2, segmentStartY),
                    size = Size(segmentSize.toPx(), segmentSize.toPx()),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = backgroundColor,
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(widthPx - segmentSize.toPx() / 2 + halfStrokePx, segmentStartY + halfStrokePx),
                    size = Size(segmentSize.toPx() - halfStrokePx * 2, segmentSize.toPx() - halfStrokePx * 2),
                    style = Fill
                )
                drawLine(
                    color = strokeColor,
                    start = Offset(widthPx, segmentStartY + segmentSize.toPx() + 1f),
                    end = Offset(widthPx, segmentStartY + (segmentSize * 1.5f).toPx() - 1f),
                    strokeWidth = strokeWidth.toPx(),
                    cap = StrokeCap.Butt
                )
            }

            // Horizontals
            drawLine(
                color = strokeColor,
                start = Offset(0f, 0f),
                end = Offset(widthPx, 0f),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            drawLine(
                color = strokeColor,
                start = Offset(0f, heightPx),
                end = Offset(widthPx, heightPx),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
        }

        content()
    }
}