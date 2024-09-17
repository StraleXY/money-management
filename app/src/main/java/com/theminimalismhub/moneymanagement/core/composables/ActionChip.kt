package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import kotlin.math.roundToInt

@Composable
fun ActionChip(
    modifier: Modifier = Modifier.padding(5.dp),
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = MaterialTheme.colors.onBackground,
    accentColor: Color = textColor,
    backgroundColor: Color = accentColor,
    borderColor: Color = accentColor,
    backgroundTint: Color = MaterialTheme.colors.secondaryVariant,
    borderThickness: Dp = 1.dp,
    backgroundStrength: Float = 0.15f,
    borderStrength: Float = 0.8f,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val animatedAlpha = animateFloatAsState(targetValue = if(enabled) 1f else 0.5f, tween(100))
    Card(
        modifier = modifier
            .height(38.dp)
            .alpha(animatedAlpha.value),
        shape = RoundedCornerShape(30.dp),
        border = if(borderThickness == 0.dp) null else BorderStroke(borderThickness, Color(ColorUtils.blendARGB(Color.Black.toArgb(), borderColor.toArgb(), borderStrength))),
        elevation = Dp(if(backgroundStrength == 0f) 0f else 6f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if(backgroundStrength == 0f) Color.Transparent else Color(ColorUtils.blendARGB(backgroundTint.toArgb(), backgroundColor.toArgb(), backgroundStrength)))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if(enabled) LocalIndication.current else null
                ) { if(enabled) onClick() }
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = textColor,
                    modifier = Modifier
                        .padding(start = 13.dp)
                        .padding(end = 9.dp)
                        .size(20.dp)
                        .alpha(0.8f)
                )
            }
            Text(
                text = text,
                color = textColor,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(bottom = 1.dp)
                    .padding(end = if (icon != null) 16.dp else 32.dp)
                    .padding(start = if (icon != null) 0.dp else 32.dp),
                textAlign = TextAlign.Center,
                style = textStyle
            )
        }
    }
}

@Composable
fun SelectableChip(
    modifier: Modifier = Modifier.widthIn(60.dp, 180.dp),
    label: String,
    icon: ImageVector? = null,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = animateColorAsState(targetValue = if(selected) MaterialTheme.colors.onSurface else MaterialTheme.colors.secondaryVariant, tween(durationMillis = 250))
    val textColor = animateColorAsState(targetValue = if(selected) MaterialTheme.colors.background else MaterialTheme.colors.onBackground, tween(durationMillis = 250))

    ActionChip(
        modifier = modifier,
        text = label,
        textColor = textColor.value,
        backgroundColor = backgroundColor.value,
        borderThickness = 0.dp,
        onClick = onClick,
        backgroundStrength = 1f,
        icon = icon
    )
}