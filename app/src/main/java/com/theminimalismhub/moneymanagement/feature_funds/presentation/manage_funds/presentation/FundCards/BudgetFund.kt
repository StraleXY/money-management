package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.composables.FadedAnimatedVisibility
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.getShadedColor
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

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
    name: String? = null,
    categories: List<Category>,
    onSelected: (Category) -> Unit
) {
    val stroke: Dp = 2.dp
    var showCategories by remember { mutableStateOf(false) }

    LaunchedEffect(categories) { showCategories = false }

    Box(
       modifier = modifier
           .fillMaxWidth()
           .height(64.dp)
           .clip(RoundedCornerShape(100))
           .shadedBackground(Shade.LIGHT, RoundedCornerShape(100))
           .clickable(enabled = !showCategories) {
               if (categories.size > 1) showCategories = !showCategories
               else onSelected(categories.first())
           }
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

        FadedAnimatedVisibility(!showCategories) {
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
        FadedAnimatedVisibility(showCategories) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                item {
                    Spacer(Modifier.width(6.dp))
                    FilledIconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = { showCategories = false },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(255, 255, 255, 75)
                        )
                    ) {
                        Icon(
                            modifier = Modifier.scale(0.95f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                }
                items(categories) { item ->
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .background(Color(255, 255, 255, 75), RoundedCornerShape(100))
                            .clip(RoundedCornerShape(100))
                            .clickable {
                                showCategories = false
                                onSelected(item)
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Center),
                            text = item.name,
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                }
            }
        }
    }
}

@Composable
fun CompactBudgetFundNoUse(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(100))
            .shadedBackground(Shade.LIGHT, RoundedCornerShape(100))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.alpha(0.75f),
                text = "Budgeting Disabled",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                color = colors.onBackground
            )
        }
    }
}


@Composable
fun CompactBudgetFundNoMatch(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(100))
            .border(2.dp, getShadedColor(Shade.LIGHT), RoundedCornerShape(100))
            .background(colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.alpha(0.75f),
                text = "No Matching Budgets",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium),
                color = colors.onBackground
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