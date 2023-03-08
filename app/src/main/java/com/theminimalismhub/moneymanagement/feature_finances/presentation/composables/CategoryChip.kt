package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ActionChip

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onToggled: (Boolean) -> Unit = {},
    isToggled: Boolean = false
) {
    val backgroundStrength = animateFloatAsState(targetValue = if(isToggled) 1f else 0.13f, tween(durationMillis = 300))
    val backgroundColor = animateColorAsState(targetValue = if(isToggled) color else Color.White, tween(durationMillis = 250))
    val textColor = animateColorAsState(targetValue = if(isToggled) Color.Black else Color.White, tween(durationMillis = 250))

    ActionChip(
        modifier = modifier,
        text = text,
        textColor = textColor.value,
        backgroundColor = backgroundColor.value,
        borderThickness = 0.dp,
        onClick = {
            onToggled(!isToggled)
        },
        backgroundStrength = backgroundStrength.value
    )
}