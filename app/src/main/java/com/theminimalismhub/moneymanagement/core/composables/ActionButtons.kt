package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.effects.ClickedState
import com.theminimalismhub.moneymanagement.core.effects.alphaClickEffect

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .alphaClickEffect()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(34.dp),
            modifier = Modifier
                .width(30.dp)
                .aspectRatio(1f)
                .alpha(0.85f),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 0.dp
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(5.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun HoldableActionButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.h4,
    icon: ImageVector,
    onHold: () -> Unit,
    onStarted: () -> Unit = {},
    onCanceled: () -> Unit = {},
    duration: Int,
    circleColor: Color = MaterialTheme.colors.primary,
    alternatedColor: Color = MaterialTheme.colors.onPrimary,
    iconColor: Color = MaterialTheme.colors.onSecondary,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    var inProgress by remember { mutableStateOf(false) }
    val animatedIconColor by animateColorAsState(targetValue = if (inProgress) alternatedColor else iconColor,
        tween(if (inProgress) duration else 200))
    val progress by animateFloatAsState(targetValue = if(inProgress) 1f else 0f,
        tween(if (inProgress) duration else 200))

    Row (
        modifier = modifier
            .fillMaxWidth()
            .alpha(if(enabled) 1f else 0.5f)
            .customHoldClickEffect(
                duration,
                enabled = enabled,
                onHold = {
                    inProgress = false
                    onHold()
                },
                onStarted = {
                    inProgress = true
                    onStarted()
                },
                onCanceled = {
                    inProgress = false
                    onCanceled()
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(34.dp),
            modifier = Modifier
                .width(30.dp)
                .aspectRatio(1f)
                .alpha(0.85f),
            backgroundColor = circleColor,
            elevation = 0.dp
        ) {
            Box {
                Icon(
                    icon,
                    contentDescription = text,
                    tint = animatedIconColor,
                    modifier = Modifier.padding(5.dp)
                )
                CircularProgressIndicator (
                    progress = progress,
                    modifier = Modifier
                        .fillMaxSize(),
                    color = alternatedColor,
                    strokeWidth = 2.5.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        Spacer(modifier = Modifier.width(if (circleColor != Color.Transparent) 16.dp else 4.dp))
        Text(
            text = text,
            color = iconColor,
            style = textStyle
        )
    }
}

fun Modifier.customHoldClickEffect(
    duration: Int,
    onHold: () -> Unit,
    onStarted: () -> Unit,
    onCanceled: () -> Unit,
    enabled: Boolean = true
) = composed {
    val haptic = LocalHapticFeedback.current
    var clickedState by remember { mutableStateOf(ClickedState.Idle) }
    val progress by animateIntAsState(if (clickedState == ClickedState.Pressed) 100 else 0, tween(if (clickedState == ClickedState.Pressed) duration else 200))

    this
        .graphicsLayer {
            if (progress == 100 && clickedState == ClickedState.Pressed) {
                clickedState = ClickedState.Idle
                haptic.performHapticFeedback(
                    HapticFeedbackType.LongPress
                )
                onHold()
            }
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { },
            enabled = enabled
        )
        .pointerInput(clickedState) {
            if(!enabled) return@pointerInput
            awaitPointerEventScope {
                clickedState = if (clickedState == ClickedState.Pressed) {
                    waitForUpOrCancellation()
                    if (progress != 100) onCanceled()
                    ClickedState.Idle
                } else {
                    awaitFirstDown(false)
                    onStarted()
                    ClickedState.Pressed
                }
            }
        }
}