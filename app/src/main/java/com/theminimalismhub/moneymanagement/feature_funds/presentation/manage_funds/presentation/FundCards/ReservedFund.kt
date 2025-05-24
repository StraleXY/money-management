package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.utils.Currencier

@Composable
fun ReservedFund(
    modifier: Modifier = Modifier,
    amount: Double? = null,
    item: String? = null,
    accountName: String? = null,
    accountIcon: ImageVector,
    categoryName: String? = null,
    categoryColor: Color,
    currency: String = "RSD"
) {
    CouponContainer(
        modifier = modifier,
        segmentsCount = 9,
        accentColor = animateColorAsState(categoryColor, tween(300)).value,
        accentText = categoryName
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 36.dp, top = 30.dp, bottom = 30.dp, end = 72.dp),
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    modifier = Modifier.alpha(if(amount == null) 0.35f else 1f),
                    text = if(amount != null) "${Currencier.formatAmount(amount.toInt())} $currency" else "Amount",
                    style = MaterialTheme.typography.h2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.alpha(0.7f).padding(bottom = 2.dp),
                    text = "RESERVED",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = "FOR",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.alpha(if(item.isNullOrEmpty()) 0.35f else 1f),
                    text = item ?: "Item Name",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SelectableChip(
                label = accountName ?: "Account Name",
                contentAlpha = if(accountName.isNullOrEmpty()) 0.4f else 1f,
                icon = accountIcon,
                onClick = {},
                selected = true,
                elevationEnabled = false
            )
        }
    }
}

@Composable
fun CouponContainer(
    modifier: Modifier = Modifier,
    segmentsCount: Int = 6,
    segmentSize: Dp = 14.dp,
    strokeWidth: Dp = 2.dp,
    accentWidth: Dp = 60.dp,
    surfaceColor: Color = MaterialTheme.colors.surface,
    strokeColor: Color = MaterialTheme.colors.secondaryVariant,
    backgroundColor: Color = MaterialTheme.colors.background,
    accentColor: Color = MaterialTheme.colors.secondaryVariant,
    accentText: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(segmentSize * 1.5f * segmentsCount + (segmentSize / 2))
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(segmentSize * 1.5f * segmentsCount + (segmentSize / 2))
        ) {

            val widthPx = this.size.width
            width = this.size.width.toDp()
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
                drawLine(
                    color = accentColor,
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

            drawRect(
                color = accentColor,
                topLeft = Offset(widthPx - accentWidth.toPx(), 0f),
                size = Size(accentWidth.toPx(), heightPx)
            )

            val halfStrokeRightPx = halfStrokePx / 2

            // Covers
            for (i in 0 until segmentsCount) {
                drawArc(
                    color = backgroundColor,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(-segmentSize.toPx() / 2 + halfStrokePx, (i * (segmentSize * 1.5f).toPx() + (segmentSize / 2).toPx()) + halfStrokePx),
                    size = Size(segmentSize.toPx() - halfStrokePx * 2, segmentSize.toPx() - halfStrokePx * 2),
                    style = Fill
                )
                drawArc(
                    color = backgroundColor,
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(widthPx - segmentSize.toPx() / 2 + halfStrokeRightPx, (i * (segmentSize * 1.5f).toPx() + (segmentSize / 2).toPx()) + halfStrokeRightPx),
                    size = Size(segmentSize.toPx() - halfStrokeRightPx * 2, segmentSize.toPx() - halfStrokeRightPx * 2),
                    style = Fill
                )
            }
        }
        RotatedRibbonLabel(
            text = accentText ?: "Category",
            textAlpha = if(accentText.isNullOrEmpty()) 0.35f else 1f,
            width = width,
            accentWidth = accentWidth,
            segmentSize = segmentSize,
            segmentsCount = segmentsCount
        )
        content()
    }
}

@Composable
fun RotatedRibbonLabel(
    text: String,
    width: Dp,
    accentWidth: Dp,
    segmentSize: Dp,
    segmentsCount: Int,
    textAlpha: Float = 1f
) {
    val height = segmentSize * 1.5f * segmentsCount + (segmentSize / 2)
    val labelMinWidth = height // so the text fits diagonally
    val rotationAngle = 90f

    Box(
        modifier = Modifier
            .height(height)
            .offset(x = width + accentWidth - labelMinWidth - accentWidth / 4)
            .width(labelMinWidth + 40.dp) // give it breathing room
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(rotationAngle)
                .width(labelMinWidth) // ensure text has room
                .padding(vertical = 4.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(textAlpha),
                text = text,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = if(MaterialTheme.colors.isLight) Color.White else Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
