package com.theminimalismhub.moneymanagement.feature_settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SettingsTile(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleColor: Color = MaterialTheme.colors.onBackground,
    descriptionColor: Color = MaterialTheme.colors.primaryVariant,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .alpha(if(enabled) 1f else 0.45f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = if(enabled) rememberRipple(true) else null,
            ) { if(enabled) onClick() },
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