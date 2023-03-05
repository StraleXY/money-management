package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.ColorUtils

@Composable
fun FramelessInputField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    characterLimit: Int = 10000,
    onValueChange: (String) -> Unit,
    textColor: Color = MaterialTheme.colors.onBackground,
    hintColor: Color = textColor,
    textAlign: TextAlign? = TextAlign.Start,
    textStyle: TextStyle = MaterialTheme.typography.body1.copy(
        color = textColor,
        textAlign = textAlign
    ),
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val isHint = remember { mutableStateOf(true) }

    SideEffect {
        isHint.value = text.isEmpty()
    }

    Box (
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                if(it.length > characterLimit) return@BasicTextField
                isHint.value = it.isEmpty()
                onValueChange(it)
            },
            textStyle = textStyle.copy(
                color = textColor
            ),
            enabled = enabled,
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier.width(IntrinsicSize.Min),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        if(isHint.value) Text(text = hint, style = MaterialTheme.typography.body2, color = Color(ColorUtils.setAlphaComponent(hintColor.toArgb(), 150)))
    }
}