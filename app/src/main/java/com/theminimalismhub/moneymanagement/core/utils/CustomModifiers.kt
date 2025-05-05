package com.theminimalismhub.moneymanagement.core.utils

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

enum class Shade(val dark: Float, val light: Float) {
    DARK(0.08f, 0.04f),
    MID(0.14f, 0.3f),
    LIGHT(0.16f, 0.2f),
}

fun Modifier.shadedBackground(shade: Shade, shape: Shape? = null): Modifier = composed {
    val color = if (MaterialTheme.colors.isLight) Color(ColorUtils.blendARGB(MaterialTheme.colors.surface.toArgb(), Color.Black.toArgb(), shade.light))
    else MaterialTheme.colors.surface.copy(1f, shade.dark, shade.dark, shade.dark)
    if(shape != null) this.background(color, shape)
    else this.background(color)
}