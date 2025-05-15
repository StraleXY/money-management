package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.utils.Currencier

@Composable
fun SavingsFund(
    modifier: Modifier = Modifier
) {
    DoubleLineCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = Currencier.formatAmount(25000),
                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.padding(bottom = 4.dp).alpha(0.5f),
                    text = "/",
                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    modifier = Modifier.padding(bottom = 6.dp).alpha(0.6f),
                    text = "${Currencier.formatAmount(100000)} RSD",
                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium, fontSize = 24.sp),
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = "SAVED FOR",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Vacation",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun DoubleLineCard(
    modifier: Modifier = Modifier,
    outerSurfaceColor: Color = MaterialTheme.colors.background,
    innerSurfaceColor: Color = MaterialTheme.colors.surface,
    outerStrokeColor: Color = MaterialTheme.colors.secondaryVariant,
    innerStrokeColor: Color = MaterialTheme.colors.secondary,
    content: @Composable BoxScope.() -> Unit
) {

    val outerStrokeWidth: Dp = 2.dp
    val innerStrokeWidth: Dp = 3.dp
    val outerRadius: Dp = 16.dp
    val innerRadius: Dp = 8.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {

        Canvas(
            modifier = modifier
                .fillMaxSize()
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            drawRoundRect(
                color = outerSurfaceColor,
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(outerRadius.toPx(), outerRadius.toPx()),
                size = Size(widthPx, heightPx)
            )
            drawRoundRect(
                color = outerStrokeColor,
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(outerRadius.toPx(), outerRadius.toPx()),
                size = Size(widthPx, heightPx),
                style = Stroke(width = outerStrokeWidth.toPx())
            )

            drawRoundRect(
                color = innerSurfaceColor,
                topLeft = Offset((8.dp + innerStrokeWidth).toPx(), (8.dp + innerStrokeWidth).toPx()),
                cornerRadius = CornerRadius(innerRadius.toPx(), innerRadius.toPx()),
                size = Size(widthPx - (8.dp + innerStrokeWidth).toPx() * 2, heightPx - (8.dp + innerStrokeWidth).toPx() * 2)
            )
            drawCircle(
                color = outerSurfaceColor,
                radius = 21.dp.toPx(),
                center = Offset(widthPx - 22.dp.toPx() - outerStrokeWidth.toPx(), 22.dp.toPx() + outerStrokeWidth.toPx())
            )
            drawCircle(
                color = outerSurfaceColor,
                radius = 5.dp.toPx(),
                center = Offset(widthPx - 44.dp.toPx() - outerStrokeWidth.toPx(), 8.dp.toPx() + outerStrokeWidth.toPx())
            )
            drawCircle(
                color = outerSurfaceColor,
                radius = 5.dp.toPx(),
                center = Offset(widthPx - 8.dp.toPx() - outerStrokeWidth.toPx(), 44.dp.toPx() + outerStrokeWidth.toPx())
            )

            drawArc(
                color = innerStrokeColor,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(widthPx - (36.dp + 8.dp).toPx(), 0f),
                size = Size(44.dp.toPx(), 44.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = innerStrokeColor,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(widthPx - (36.dp + 8.dp + 16.dp).toPx(), (12).dp.toPx()),
                size = Size(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = innerStrokeColor,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(widthPx - (36.dp + 8.dp - 16.dp).toPx(), (44).dp.toPx()),
                size = Size(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // T L
            drawLine(
                color = innerStrokeColor,
                start = Offset(20.dp.toPx(), 12.dp.toPx()),
                end = Offset(widthPx - 52.dp.toPx(), 12.dp.toPx()),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            drawArc(
                color = innerStrokeColor,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset((12.dp).toPx(), (12).dp.toPx()),
                size = Size(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // B L
            drawLine(
                color = innerStrokeColor,
                start = Offset(12.dp.toPx(), 20.dp.toPx()),
                end = Offset(12.dp.toPx(), heightPx - 20.dp.toPx()),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            drawArc(
                color = innerStrokeColor,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset((12.dp).toPx(), heightPx - (28).dp.toPx()),
                size = Size(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // B R
            drawLine(
                color = innerStrokeColor,
                start = Offset(20.dp.toPx(), heightPx - 12.dp.toPx()),
                end = Offset(widthPx - 20.dp.toPx(), heightPx - 12.dp.toPx()),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            drawArc(
                color = innerStrokeColor,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(widthPx - (28.dp).toPx(), heightPx - (28).dp.toPx()),
                size = Size(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = innerStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawLine(
                color = innerStrokeColor,
                start = Offset(widthPx - 12.dp.toPx(), 52.dp.toPx()),
                end = Offset(widthPx - 12.dp.toPx(), heightPx - 20.dp.toPx()),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )

//            drawRoundRect(
//                color = innerSurfaceColor,
//                topLeft = Offset(strokePadding, strokePadding),
//                cornerRadius = CornerRadius(innerRadius.toPx(), innerRadius.toPx()),
//                size = Size(widthPx - strokePadding * 2, heightPx - strokePadding * 2)
//            )
//            drawRoundRect(
//                color = innerStrokeColor,
//                topLeft = Offset(strokePadding, strokePadding),
//                cornerRadius = CornerRadius(innerRadius.toPx(), innerRadius.toPx()),
//                size = Size(widthPx - strokePadding * 2, heightPx - strokePadding * 2),
//                style = Stroke(width = innerStrokeWidth.toPx())
//            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-7).dp, y = 7.dp)
                .size(30.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(innerStrokeColor)
        ) {
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .scale(0.8f)
                    .size(26.dp),
                imageVector = Icons.Default.Savings,
                contentDescription = "",
                tint = innerSurfaceColor
            )
        }

        content()
    }
}