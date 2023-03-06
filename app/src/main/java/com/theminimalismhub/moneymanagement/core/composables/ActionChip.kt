package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlin.math.roundToInt

@Composable
fun ActionChip(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = Color.White,
    accentColor: Color = textColor,
    backgroundColor: Color = accentColor,
    borderColor: Color = accentColor,
    borderThickness: Dp = 1.dp,
    backgroundStrength: Float = 0.15f,
    borderStrength: Float = 0.8f,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val animatedAlpha = animateFloatAsState(targetValue = if(enabled) 1f else 0.5f)
    Card(
        modifier = modifier
            .padding(5.dp)
            .height(38.dp)
            .alpha(animatedAlpha.value),
        elevation = Dp(if(backgroundStrength == 0f) 0f else 8f),
        shape = RoundedCornerShape(30.dp),
        border = if(borderThickness == 0.dp) null else BorderStroke(borderThickness, Color(ColorUtils.blendARGB(Color.Black.toArgb(), borderColor.toArgb(), borderStrength))),
        backgroundColor = if(backgroundStrength == 0f) Color.Transparent else Color(ColorUtils.setAlphaComponent(backgroundColor.toArgb(),
            (backgroundStrength * 255L).roundToInt()
        ))

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if(enabled) LocalIndication.current else null
                ) {
                    if(enabled) onClick()
                }
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