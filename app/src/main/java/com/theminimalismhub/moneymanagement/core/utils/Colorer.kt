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
            if(MaterialTheme.colors.isLight) {
                // Option 2 - Simple Black Overlay
                 return Color(ColorUtils.blendARGB(color, Color(0.05f, 0.05f, 0.05f).toArgb(), color.luminance/2.5f))

                // Option 3 - Something else
//                return darkenColor(Color(color), 0.25f)
            }
            return Color(color)
        }

        fun darkenColor(color: Color, fraction: Float): Color {
            val red = color.red * (1 - fraction)
            val green = color.green * (1 - fraction)
            val blue = color.blue * (1 - fraction)
            return Color(red, green, blue)
        }
    }
}