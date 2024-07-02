package com.theminimalismhub.moneymanagement.core.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.luminance

class Colorer {
    companion object {

        @Composable
        fun getAdjustedDarkColor(color: Int) : Color {
            return if (MaterialTheme.colors.isLight) Color(ColorUtils.blendARGB(color, Color(0.05f, 0.05f, 0.05f).toArgb(), color.luminance/2.5f))
            else Color(color)
        }

        fun getAdjustedDarkColor(color: Int, isLight: Boolean) : Color {
            return if (isLight) Color(ColorUtils.blendARGB(color, Color(0.05f, 0.05f, 0.05f).toArgb(), color.luminance/2.5f))
            else Color(color)
        }

        fun darkenColor(color: Color, fraction: Float): Color {
            val red = color.red * (1 - fraction)
            val green = color.green * (1 - fraction)
            val blue = color.blue * (1 - fraction)
            return Color(red, green, blue)
        }
    }
}