package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.graphics.ColorUtils

@Composable
fun TranslucentOverlay(
    visible: Boolean
) {
    val focusManager = LocalFocusManager.current
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(tween(350))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if(MaterialTheme.colors.isLight) Color(ColorUtils.setAlphaComponent(MaterialTheme.colors.secondaryVariant.toArgb(), (0.95f * 255L).toInt()))
                    else Color(ColorUtils.setAlphaComponent(MaterialTheme.colors.surface.toArgb(), (0.95f * 255L).toInt()))
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus(true) }
        ) { }
    }
}