package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
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
fun BudgetCard(

) {
    CreditCardColored(
        colors = listOf(Color.Yellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier,
                    text = "MONTHLY",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Black)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = Currencier.formatAmount(4740),
                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .alpha(0.5f),
                    text = "/",
                    style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.ExtraLight),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(2.dp))
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .alpha(0f),
                        text = "Left Of",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .alpha(0.6f),
                        text = "${Currencier.formatAmount(5000)} RSD",
                        style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium, fontSize = 24.sp),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(72.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = "BUDGET FOR",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Coffee",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
fun CreditCardColored(
    modifier: Modifier = Modifier,
    strokeColor: Color = MaterialTheme.colors.secondaryVariant,
    surfaceColor: Color = MaterialTheme.colors.surface,
    stripHeight: Dp = 48.dp,
    colors: List<Color> = listOf(),
    content: @Composable BoxScope.() -> Unit
) {

    val height: Dp = 200.dp
    val radius: Dp = 16.dp
    val outerStrokeWidth: Dp = 2.dp
    val innerStrokeWidth: Dp = 2.dp
    val bottomOffset: Dp = 46.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 24.dp)
    ) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            drawRoundRect(
                color = surfaceColor,
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                size = Size(widthPx, heightPx)
            )
            drawRoundRect(
                color = strokeColor,
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                size = Size(widthPx, heightPx),
                style = Stroke(width = outerStrokeWidth.toPx())
            )
            drawLine(
                color = strokeColor,
                start = Offset(0f, heightPx - bottomOffset.toPx() - stripHeight.toPx() - innerStrokeWidth.toPx() / 2),
                end = Offset(widthPx, heightPx - bottomOffset.toPx() - stripHeight.toPx() - innerStrokeWidth.toPx() / 2),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
            drawLine(
                color = strokeColor,
                start = Offset(0f, heightPx - bottomOffset.toPx() + innerStrokeWidth.toPx() / 2),
                end = Offset(widthPx, heightPx - bottomOffset.toPx() + innerStrokeWidth.toPx() / 2),
                strokeWidth = innerStrokeWidth.toPx(),
                cap = StrokeCap.Butt
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(stripHeight)
                .offset(y = height - stripHeight - bottomOffset)
                .blur(if (colors.size == 1) 24.dp else if(colors.size == 2) 250.dp else 124.dp, BlurredEdgeTreatment.Rectangle)
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            drawRect(
                color = Color.Gray,
                topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
            )

            val count = colors.size
            if (count == 1) {
                drawRect(
                    color = colors.first(),
                    topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                    size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
                )
            }
            if (count <= 1)return@Canvas

            val radius = (widthPx / count) * 1.25f
            val spacing = widthPx / count

            for(i in 0 until count) {
                drawCircle(
                    color = colors[i],
                    radius = radius,
                    center = Offset(spacing * (i + 1), if(i % 2 == 0) radius / 3 + heightPx else -radius / 3)
                )
            }

            drawRect(
                color = Color.Black,
                topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
                alpha = 0.35f
            )
        }
        content()
    }
}