package com.theminimalismhub.moneymanagement.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.R

val anton = FontFamily(
    Font(R.font.anton_regular)
)

val economica = FontFamily(
    Font(R.font.economica)
)

val titilium = FontFamily(
    Font(R.font.tw_regular),
    Font(R.font.tw_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.tw_extra_light, FontWeight.ExtraLight, FontStyle.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = economica,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 50.sp
    ),
    h2 = TextStyle(
        fontFamily = economica,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 50.sp
    ),
    h3 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp
    ),
    h4 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    h5 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Light,
        fontSize = 17.sp,
        letterSpacing = 0.75.sp
    ),
    body1 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    ),
    body2 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Light,
        fontSize = 17.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.Light,
        fontSize = 15.sp,
        lineHeight = 18.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = titilium,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 15.sp,
        lineHeight = 18.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 3.sp
    )
)