package com.theminimalismhub.moneymanagement.feature_settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsc.form_builder.TextFieldState
import org.w3c.dom.Text

@Composable
fun SettingsTile(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleColor: Color = colors.onBackground,
    descriptionColor: Color = colors.primaryVariant,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = if (enabled) rememberRipple(true) else null,
            ) { if (enabled) onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title.uppercase(),
                color = titleColor,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
            Text(
                text = description,
                color = descriptionColor,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
            )
        }

        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = titleColor,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(0.8f)
            )
        }
    }
}


@Composable
fun SettingsTile(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleColor: Color = colors.onBackground,
    descriptionColor: Color = colors.primaryVariant,
    enabled: Boolean = true,
    fieldState: TextFieldState,
    inputType: KeyboardType,
    onValChanged: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .alpha(if (enabled) 1f else 0.45f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier.widthIn(max = 260.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title.uppercase(),
                color = titleColor,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
            Text(
                text = description,
                color = descriptionColor,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        OutlinedTextField(
            value = fieldState.value,
            onValueChange = {
                fieldState.change(it)
                onValChanged(it)
            },
            modifier = Modifier,
            textStyle = MaterialTheme.typography.body1.copy(fontSize = 19.sp, textAlign = TextAlign.Center),
//            label = { Text(text = "Amount") },
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = inputType, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.secondaryVariant,
                focusedBorderColor = colors.primaryVariant
            )
        )
    }
}

@Composable
fun SettingsTile(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleColor: Color = colors.onBackground,
    descriptionColor: Color = colors.primaryVariant,
    enabled: Boolean = true,
    toggled: Boolean = false,
    onToggle: (Boolean) -> Unit
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .alpha(if (enabled) 1f else 0.45f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier.widthIn(max = 248.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title.uppercase(),
                color = titleColor,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = description,
                color = descriptionColor,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Checkbox(
            checked = toggled,
            onCheckedChange = onToggle,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary,
                uncheckedColor = MaterialTheme.colors.primary
            )
        )
    }
}