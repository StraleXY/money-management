package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
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
    val backgroundColor = animateColorAsState(targetValue = if(isToggled) color else MaterialTheme.colors.secondaryVariant, tween(durationMillis = 250))
    val textColor = animateColorAsState(targetValue = if(isToggled) Color.Black else MaterialTheme.colors.onSurface, tween(durationMillis = 250))

    ActionChip(
        modifier = modifier,
        text = text,
        textColor = textColor.value,
        backgroundColor = backgroundColor.value,
        borderThickness = 0.dp,
        onClick = { onToggled(!isToggled) },
        backgroundStrength = 1f
    )
}