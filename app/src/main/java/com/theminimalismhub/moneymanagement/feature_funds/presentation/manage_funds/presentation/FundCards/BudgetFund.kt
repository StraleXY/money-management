package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground

@Composable
fun BudgetFund(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(),
    recurring: String? = null,
    remaining: Double? = null,
    amount: Double? = null,
    name: String? = null
) {
    CreditCardColored(
        modifier = modifier,
        colors = colors
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
                    modifier = Modifier.alpha(if(recurring.isNullOrEmpty()) 0.35f else 1f),
                    text = recurring ?: "RECURRING",
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
                    modifier = Modifier.alpha(if(remaining == null) 0.35f else 1f),
                    text = if(remaining != null) Currencier.formatAmount(remaining) else "Remaining",
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
                            .alpha(if(amount == null) 0.35f else 0.6f),
                        text = if(amount != null) "${Currencier.formatAmount(amount)} RSD" else "Amount",
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
                    modifier = Modifier.alpha(if(name.isNullOrEmpty()) 0.35f else 1f),
                    text = name ?: "Items",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
fun CompactBudgetFund(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(),
    recurring: String? = null,
    remaining: Double? = null,
    amount: Double? = null,
    name: String? = null
) {
    val stroke: Dp = 2.dp

    Box(
       modifier = modifier
           .fillMaxWidth()
           .height(64.dp)
           .clip(RoundedCornerShape(100))
//           .border(2.dp, colors.firstOrNull() ?: Color.LightGray, RoundedCornerShape(100))
           .shadedBackground(Shade.LIGHT, RoundedCornerShape(100))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (colors.isEmpty()) 0.dp else if (colors.size == 1) 8.dp else if(colors.size == 2) 224.dp else 100.dp, BlurredEdgeTreatment.Rectangle)
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            val count = colors.size
            if (count == 0) {
                drawRect(
                    color = Color.LightGray,
                    topLeft = Offset(0f, 0f),
                    size = Size(widthPx, heightPx),
                )
            }
            else if (count == 1) {
                drawRect(
                    color = colors.first(),
                    topLeft = Offset(0f, 0f),
                    size = Size(widthPx, heightPx),
                )
            }
            else {
                val radius = (widthPx / count) * 1.25f
                val spacing = widthPx / count

                for(i in 0 until count) {
                    drawCircle(
                        color = colors[i],
                        radius = radius,
                        center = Offset(spacing * (i + 1), if(i % 2 == 0) radius / 3 + heightPx else -radius / 3)
                    )
                }
            }

            drawRect(
                color = Color.Black,
                topLeft = Offset(0f, 0f),
                size = Size(widthPx, heightPx),
                alpha = 0.25f
            )
        }


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    modifier = Modifier.alpha(if(remaining == null) 0.35f else 1f),
                    text = if(remaining != null) Currencier.formatAmount(remaining) else "Remaining",
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .alpha(0.5f),
                    text = "/",
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.ExtraLight),
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    modifier = Modifier
                        .alpha(if(amount == null) 0.35f else 0.6f),
                    text = if(amount != null) "${Currencier.formatAmount(amount)} RSD" else "Amount",
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    color = MaterialTheme.colors.onBackground
                )
            }
            Text(
                text = name ?: "Unknown",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun CreditCardColored(
    modifier: Modifier = Modifier,
    strokeColor: Color = MaterialTheme.colors.secondaryVariant,
    surfaceColor: Color = MaterialTheme.colors.surface,
    bottomSurfaceColor: Color = MaterialTheme.colors.background,
    stripHeight: Dp = 48.dp,
    colors: List<Color> = listOf(),
    content: @Composable BoxScope.() -> Unit
) {

    val height: Dp = 200.dp
    val radius: Dp = 16.dp
    val outerStrokeWidth: Dp = 2.dp
    val bottomOffset: Dp = 48.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
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
                color = bottomSurfaceColor,
                topLeft = Offset(0f, heightPx - stripHeight.toPx() / 2 - bottomOffset.toPx()),
                cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                size = Size(widthPx, stripHeight.toPx() / 2 + bottomOffset.toPx()),
            )
            drawRoundRect(
                color = strokeColor,
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                size = Size(widthPx, heightPx),
                style = Stroke(width = outerStrokeWidth.toPx())
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(stripHeight)
                .offset(y = height - stripHeight - bottomOffset)
                .blur(if (colors.isEmpty()) 0.dp else if (colors.size == 1) 24.dp else if(colors.size == 2) 250.dp else 124.dp, BlurredEdgeTreatment.Rectangle)
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            drawRect(
                color = Color.Black,
                topLeft = Offset(outerStrokeWidth.toPx(), 0f),
                size = Size(widthPx - outerStrokeWidth.toPx() * 2, heightPx),
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(stripHeight)
                .offset(y = height - stripHeight - bottomOffset)
                .blur(if (colors.isEmpty()) 0.dp else if (colors.size == 1) 8.dp else if(colors.size == 2) 224.dp else 100.dp, BlurredEdgeTreatment.Rectangle)
        ) {
            val widthPx = this.size.width
            val heightPx = this.size.height

            drawRect(
                color = Color.Black,
                topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
            )

            val count = colors.size
            if (count == 0) {
                drawRect(
                    color = strokeColor,
                    topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                    size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
                )
            }
            if (count == 1) {
                drawRect(
                    color = colors.first(),
                    topLeft = Offset(outerStrokeWidth.toPx() / 2, 0f),
                    size = Size(widthPx - outerStrokeWidth.toPx(), heightPx),
                )
            }
            if (count <= 0)return@Canvas

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
                alpha = 0.25f
            )
        }
        content()
    }
}


@Composable
fun RecurringTypeSelector(
    modifier: Modifier = Modifier,
    selectedRecurringType: RecurringType?,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    spacing: Dp = 8.dp,
    onRecurringTypeSelected: (RecurringType) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.Start,
        state = listState
    ) {
        items(RecurringType.values()) { type ->
            SelectableChip(
                label = type.label,
                selected = selectedRecurringType == type
            ) { onRecurringTypeSelected(type) }
            Spacer(modifier = Modifier.width(spacing))
        }
    }
}