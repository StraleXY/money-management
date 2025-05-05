package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import android.util.Half.toFloat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.rememberPagerState
import com.theminimalismhub.moneymanagement.core.enums.RangeType
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_finances.domain.utils.RangePickerService
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.absoluteValue

@Composable
fun QuickSpendingOverview(
    modifier: Modifier = Modifier,
    amount: Double,
    rangeLength: Int,
    limit: Double = 0.0,
    limitHidden: Boolean = false,
    currency: String = "RSD"
) {
    var width by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Card(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                width = with(density) {
                    coordinates.size.width.toDp()
                }
            },
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(125.dp),
                title = if(limitHidden) "TOTAL" else "SPENT",
                amount = amount,
                secondaryAmounts = listOf(Pair("AVERAGE", if(rangeLength == 1) 0.0 else amount / rangeLength)),
                currency = currency
            )
            AnimatedVisibility(
                visible = !limitHidden,
                enter = expandHorizontally(tween(300)) { 0 } + fadeIn(tween(200, 100)),
                exit = fadeOut(tween(200)) + shrinkHorizontally(tween(300, 100)) { 0 }
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(125.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                SpendingSegment(
                    modifier = Modifier
//                        .weight(0.49f, true)
                        .width(width / 2)
                        .height(125.dp),
                    title = "REMAINING",
                    amount = limit * rangeLength - amount,
                    secondaryAmounts = listOf(Pair("LIMIT", limit * rangeLength)),
                    currency = currency
                )
            }

        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun SpendingSegment(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    currency: String = "RSD",
    secondaryAmounts: List<Pair<String, Double>> = emptyList()
) {
    val animatedAmount by
        if(Currencier.isDecimal(amount)) animateFloatAsState(targetValue = amount.toFloat(), tween(750))
        else animateIntAsState(targetValue = amount.toInt(), tween(750))

    val animatedSecondaryAmounts = secondaryAmounts.map {
        animateFloatAsState(targetValue = it.second.toFloat(), tween(750), label = "amount_${it.first}")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${Currencier.formatAmount(animatedAmount.toFloat(), true)} $currency ",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 45.sp
            ),
            color = if(amount < 0.0) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        secondaryAmounts.forEachIndexed { idx, secondary ->
            Text(
                modifier = Modifier
                    .alpha(0.65f),
                text = "${secondary.first}: ${if (secondary.second == 0.0) "--" else Currencier.formatAmount(if(Currencier.isDecimal(secondary.second)) animatedSecondaryAmounts[idx].value.toDouble() else animatedSecondaryAmounts[idx].value.toInt().toDouble(), true)} $currency",
                style = MaterialTheme.typography.h3.copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun QuickSpendingOverviewCompact(
    exampleDate: String,
    modifier: Modifier = Modifier,
    amount: Double,
    average: Double,
    rangeLength: Int,
    limit: Double = 0.0,
    limitHidden: Boolean = false,
    currency: String = "RSD",
    dateSelectable: Boolean = false,
    dateClicked: (() -> Unit)? = null,
    selectedCategory: CategoryAmount? = null,
    contentAlpha: Float = 1f,
    isExpanded: Boolean? = null,
    onToggle: (() -> Unit)? = null
) {
    fun increase() : Int {
        return try {
            ((amount - average * rangeLength) / (average * rangeLength) * 100).toInt()
        } catch (ex: Exception) {
            0
        }
    }

    val percent by remember(amount) { mutableStateOf(increase()) }
    var expanded by remember { mutableStateOf(isExpanded ?: false) }

    LaunchedEffect(isExpanded) {
        isExpanded?.let { expanded = isExpanded }
    }

    val animatedAmount by
        if(Currencier.isDecimal(amount)) animateFloatAsState(targetValue = amount.toFloat(), tween(750))
        else animateIntAsState(targetValue = amount.toInt(), tween(750),
        label = "spendings_amount"
    )
    val animatedPercent by animateFloatAsState(targetValue =
        if(selectedCategory != null) (selectedCategory.amount / amount * 100).toInt().toFloat()
        else percent.absoluteValue.toFloat(), tween(750),
        label = "spendings_chip_value"
    )
    val animatedBackground by animateColorAsState(targetValue =
        if(selectedCategory != null) Color((Color(selectedCategory.color).red * 255).toInt(), (Color(selectedCategory.color).green * 255).toInt(), (Color(selectedCategory.color).blue * 255).toInt(), 125)
        else if(percent > 0) Color(209, 59, 21, 100)
        else Color(111, 176, 62, 100),
        label = "spendings_chip_background"
    )
    val animatedForeground by animateColorAsState(targetValue =
        if(selectedCategory != null) MaterialTheme.colors.onBackground
        else if(percent > 0) Color(232, 210, 204, 255)
        else Color(232, 245, 223, 255),
        label = "spendings_chip_foreground"
    )
    val animatedIconRotation by animateFloatAsState(targetValue =
        if(expanded) 180f else 0f,
        label = "arrow_direction"
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadedBackground(Shade.MID),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .alpha(contentAlpha)
        ) {
            Text(
                text = exampleDate,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alpha(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = dateSelectable
                    ) { dateClicked?.invoke() }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${animatedAmount.toInt()} $currency",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(12.dp))
                if(!limitHidden || selectedCategory != null) Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .background(animatedBackground),
                ) {
                    Row(
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 8.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if(selectedCategory != null) Icons.Default.Tonality else if (percent > 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = "Upper-Lower icon",
                            tint = animatedForeground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${animatedPercent.toInt()}%",
                            style = MaterialTheme.typography.body1,
                            color = animatedForeground
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically { 0 },
                exit = fadeOut() + shrinkVertically { 0 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Text(
                            modifier = Modifier.alpha(0.75f),
                            text = "Average: ",
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = "${(average * rangeLength).toInt()} $currency ",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.75f),
                            text = "Target: ",
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = "${(limit * rangeLength).toInt()} $currency ",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(14.dp)
                                .alpha(0.25f)
                                .background(MaterialTheme.colors.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(amount - (limit * rangeLength)).absoluteValue.toInt()} $currency",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if(amount > limit * rangeLength) "Over Budget" else "Remaining",
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.ExtraBold,
                            color = if(amount > limit * rangeLength) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
        Row(modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(8.dp)) {
            IconButton(
                onClick = {
                    if(onToggle != null) onToggle()
                    else expanded = !expanded
                }
            ) {
                Icon(
                    modifier = Modifier.rotate(-animatedIconRotation),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand-Shrink"
                )
            }
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRangePicker(
    rangeService: RangePickerService,
    rangePicked: (Pair<Long, Long>, Boolean) -> Unit,
    isToday: Boolean = true,
    amount: Double,
    average: Double,
    limit: Double,
    limitHidden: Boolean,
    currency: String,
    selectedCategory: CategoryAmount?
) {
    val PREVIOUS_PAGE = 0
    val CENTER_PAGE = 1
    val NEXT_PAGE = 2

    val scope = rememberCoroutineScope()
    val ranges = remember { mutableStateListOf(rangeService.getPreviousPair(), rangeService.getCurrentPair(), rangeService.getNextPair()) }
    val scrollState = rememberPagerState(pageCount = 3, initialPage = CENTER_PAGE)
    val currentPage by remember { derivedStateOf { scrollState.currentPage } }

    fun updatePages() {
        scope.launch {
            ranges[CENTER_PAGE] = rangeService.getCurrentPair()
            delay(5)
            scrollState.scrollToPage(CENTER_PAGE)
            delay(5)
            ranges[PREVIOUS_PAGE] = rangeService.getPreviousPair()
            ranges[NEXT_PAGE] = rangeService.getNextPair()
        }
    }

    val animatedScale = remember { Animatable(initialValue = 1f, visibilityThreshold = 0.01f) }

    fun animateUpdate() {
        scope.launch {
            animatedScale.animateTo(1f, keyframes {
                durationMillis = 400
                0.9f at 200 with EaseIn
                1f at 400 with EaseOut
            })
        }
    }

    fun sendOnUpdate(isAnimated: Boolean) {
        rangePicked(Pair(rangeService.getStartTimestamp(), rangeService.getEndTimestamp()), rangeService.isToday())
        updatePages()
        if(isAnimated) animateUpdate()
    }

    fun onSwipe() {
        when(scrollState.currentPage) {
            PREVIOUS_PAGE -> {
                rangeService.previous()
                sendOnUpdate(false)
            }
            NEXT_PAGE -> {
                rangeService.next()
                sendOnUpdate(false)
            }
        }
    }

    LaunchedEffect(currentPage) { if(currentPage != CENTER_PAGE) onSwipe() }

    val picker = datePickerDialog(
        initialTime = rangeService.getCurrentTimestamp(),
        datePicked = {
            rangeService.set(it)
            sendOnUpdate(true)
        }
    )

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.shadedBackground(Shade.MID)) {
        Box(modifier = Modifier.padding(top = 32.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.Center
            ) {
                ToggleButton(
                    text = "DAILY",
                    isToggled = rangeService.type == RangeType.DAILY
                ) {
                    rangeService.setModeDay()
                    sendOnUpdate(true)
                }
                ToggleButton(
                    text = "WEEKLY",
                    isToggled = rangeService.type == RangeType.WEEKLY
                ) {
                    rangeService.setModeWeek()
                    sendOnUpdate(true)
                }
                ToggleButton(
                    text = "MONTHLY",
                    isToggled = rangeService.type == RangeType.MONTHLY
                ) {
                    rangeService.setModeMonth()
                    sendOnUpdate(true)
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
            ) {
                AnimatedVisibility(
                    visible = !isToday,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { h -> -h/4 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { h -> -h/4 })
                ) {
                    IconButton(onClick = {
                        rangeService.setToday()
                        sendOnUpdate(true)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = Icons.Default.ArrowLeft.name
                        )
                    }
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.scale(animatedScale.value),
            state = scrollState,
            flingBehavior = PagerDefaults.defaultPagerFlingConfig(
                state = scrollState,
                snapAnimationSpec = tween(150),
                decayAnimationSpec = exponentialDecay(0.6f, 0.1f)
            ),
            dragEnabled = currentPage == CENTER_PAGE
        ) {
            QuickSpendingOverviewCompact(
                modifier = Modifier.padding(horizontal = 20.dp),
                exampleDate = rangeService.formatPair(ranges.toList()[it]),
                amount = amount,
                average = average,
                rangeLength = rangeService.rangeLength,
                limit = limit,
                limitHidden = limitHidden,
                currency = currency,
                dateSelectable = rangeService.type == RangeType.DAILY,
                dateClicked = {
                    picker.updateDate(
                        rangeService.getStartDay().get(Calendar.YEAR),
                        rangeService.getStartDay().get(Calendar.MONTH),
                        rangeService.getStartDay().get(Calendar.DAY_OF_MONTH)
                    )
                    picker.show()
                },
                selectedCategory = selectedCategory,
                contentAlpha = (animatedScale.value - 0.9f) * 10,
                isExpanded = expanded,
                onToggle = { expanded = !expanded}
            )
        }
    }
}