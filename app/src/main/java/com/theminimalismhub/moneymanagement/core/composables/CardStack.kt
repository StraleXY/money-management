package com.theminimalismhub.moneymanagement.core.composables

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
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun CardStack(
    modifier: Modifier = Modifier,
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
                    y = CardStack.Y_POSITION * index,
                )
            }
        }
    }
}

@Composable
fun DraggableCardWithThreshold(
    swap: () -> Unit,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()

    val maxOffset = 60.dp
    val threshold = 30.dp

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { maxOffset.toPx() }
    val thresholdPx = with(density) { threshold.toPx() }

    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            val newOffset = (offsetY.value + dragAmount.y).coerceIn(-maxOffsetPx, maxOffsetPx)
                            scope.launch {
                                offsetY.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            if(offsetY.value > thresholdPx || offsetY.value < -thresholdPx) swap()
                            scope.launch {
                                offsetY.animateTo(0f, animationSpec = spring())
                            }
                        }
                    )
                }
                .fillMaxWidth()
                .height(64.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(100)
        ) { content() }
    }
}

object CardStack {
    const val EXTRA_PADDING = 10
    const val Y_POSITION = 7
    const val X_POSITION = 0
}