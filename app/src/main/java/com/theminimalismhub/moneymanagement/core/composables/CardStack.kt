package com.theminimalismhub.moneymanagement.core.composables

import android.util.Log
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true,
    content: @Composable () -> Unit,
) {
    Layout(
        content,
        modifier,
    ) { measurables, constraints ->

        val placeables =
            measurables.map { measurable ->
                measurable.measure(constraints)
            }

        val height =
            if (placeables.isNotEmpty()) {
                placeables.first().height +
                        (CardStack.EXTRA_PADDING * placeables.size)
            } else {
                0
            }

        val width =
            if (placeables.isNotEmpty()) {
                placeables.first().width
            } else {
                0
            }

        layout(width = width, height = height) {
            placeables.mapIndexed { index, placeable ->
                placeable.place(
                    x = CardStack.X_POSITION,
                    y = ((if(isExpanded) 68.dp.toPx() else CardStack.Y_POSITION).toFloat() * index).roundToInt(),
                )
            }
        }
    }
}

@Composable
fun AnimatedCardStack(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true,
    items: List<@Composable () -> Unit>
) {
    val defaultOffset = with(LocalDensity.current) { CardStack.Y_POSITION.toDp() }

    var isExpandedDelayed by remember { mutableStateOf(isExpanded) }
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(250)
            isExpandedDelayed = true
        }
        else {
            isExpandedDelayed = false
        }
    }

    if (isExpandedDelayed) LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 48.dp, bottom = 24.dp)
    ) {
        items(items) {
            it()
            Spacer(Modifier.height(4.dp))
        }
    }
    else Box(modifier = modifier.padding(top = 48.dp)) {
        items.forEachIndexed { index, content ->
            val targetOffset = if (isExpanded) 68.dp * index else defaultOffset * index
            val offsetY by animateDpAsState(
                targetValue = targetOffset,
                animationSpec = tween(250),
                label = "CardStackOffset"
            )
            Box(modifier = Modifier.offset(y = offsetY)) { content() }
        }
    }
}

@Composable
fun DraggableCardWithThreshold(
    swap: (Float) -> Unit,
    idx: Int,
    lastIdx: Int,
    lastOffset: Float,
    enabled: Boolean,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()

    val maxOffset = 80.dp
    val threshold = 60.dp

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { maxOffset.toPx() }
    val thresholdPx = with(density) { threshold.toPx() }

    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(lastOffset) {
        if(lastOffset == 0f) return@LaunchedEffect
        if (idx == 0) {
            offsetY.snapTo(lastOffset)
            delay(10)
            offsetY.animateTo(0f, animationSpec = spring())
            swap(0f)
        }
        else {
            offsetY.snapTo(-8f)
            delay(10)
            offsetY.animateTo(0f, animationSpec = tween())
        }
    }

    LaunchedEffect(enabled) {
        Log.d("ENABLED CHANGED", enabled.toString())
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if(enabled) Card(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            val newOffset = (offsetY.value + dragAmount.y).coerceIn(-maxOffsetPx, maxOffsetPx)
                            scope.launch { offsetY.snapTo(newOffset) }
                        },
                        onDragEnd = {
                            if (offsetY.value > thresholdPx || offsetY.value < -thresholdPx) {
                                scope.launch {
                                    swap(offsetY.value)
                                    if(idx == lastIdx) offsetY.snapTo(-8f)
                                }
                            }
                            else scope.launch { offsetY.animateTo(0f, animationSpec = spring()) }
                        }
                    )
                }
                .fillMaxWidth()
                .height(64.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(100)
        ) { content() }
        else Card(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .height(64.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(100)
        ) { content() }
    }
}

object CardStack {
    const val EXTRA_PADDING = 10
    const val Y_POSITION = 8
    const val X_POSITION = 0
}