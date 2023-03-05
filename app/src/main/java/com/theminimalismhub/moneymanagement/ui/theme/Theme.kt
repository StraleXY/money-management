package com.theminimalismhub.moneymanagement.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryVariantDark,
    onPrimary = Black,
    secondary = SecondaryDark,
    secondaryVariant = SecondaryVariantDark,
    onSecondary = Black,
    background = BackgroundDark,
    onBackground = White
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = PrimaryVariantLight,
    onPrimary = White,
    secondary = SecondaryLight,
    secondaryVariant = SecondaryVariantLight,
    onSecondary = White,
    background = BackgroundLight,
    onBackground = Black
)


@Composable
fun MoneyManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}