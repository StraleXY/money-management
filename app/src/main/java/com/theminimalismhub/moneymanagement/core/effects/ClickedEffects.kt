package com.theminimalismhub.moneymanagement.core.effects

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback

enum class ClickedState { Pressed, Idle }

fun Modifier.alphaClickEffect() = composed {
    var clickedState by remember { mutableStateOf(ClickedState.Idle) }
    val alph by animateFloatAsState(if (clickedState == ClickedState.Pressed) 0.6f else 1f)

    this
        .graphicsLayer {
            alpha = alph
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(clickedState) {
            awaitPointerEventScope {
                clickedState = if (clickedState == ClickedState.Pressed) {
                    waitForUpOrCancellation()
                    ClickedState.Idle
                } else {
                    awaitFirstDown(false)
                    ClickedState.Pressed
                }
            }
        }
}

fun Modifier.scaledClickEffect(
    targetScale: Float = 0.95f
) = composed {
    var clickedState by remember { mutableStateOf(ClickedState.Idle) }
    val scale by animateFloatAsState(
        if (clickedState == ClickedState.Pressed) targetScale else 1f,
        tween(if (clickedState == ClickedState.Pressed) 10 else 375)
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(clickedState) {
            awaitPointerEventScope {
                clickedState = if (clickedState == ClickedState.Pressed) {
                    waitForUpOrCancellation()
                    ClickedState.Idle
                } else {
                    awaitFirstDown(false)
                    ClickedState.Pressed
                }
            }
        }
}

fun Modifier.alphaHoldEffect(
    duration: Int,
    onHold: () -> Unit
) = composed {
    val haptic = LocalHapticFeedback.current
    var clickedState by remember { mutableStateOf(ClickedState.Idle) }
    val alph by animateFloatAsState(if (clickedState == ClickedState.Pressed) 0.6f else 1f, tween(if (clickedState == ClickedState.Pressed) duration else 200))

    this
        .graphicsLayer {
            alpha = alph
            if (alph == 0.6f && clickedState == ClickedState.Pressed) haptic.performHapticFeedback(
                HapticFeedbackType.LongPress
            )
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(clickedState) {
            awaitPointerEventScope {
                clickedState = if (clickedState == ClickedState.Pressed) {
                    waitForUpOrCancellation()
                    if (alph == 0.6f) onHold()
                    ClickedState.Idle
                } else {
                    awaitFirstDown(false)
                    ClickedState.Pressed
                }
            }
        }
}