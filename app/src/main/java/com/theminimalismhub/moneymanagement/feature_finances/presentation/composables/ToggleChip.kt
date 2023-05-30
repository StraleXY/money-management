package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

@Composable
fun ToggleChip(
    modifier: Modifier = Modifier,
    name: String,
    icon: @Composable () -> Unit,
    toggled: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor by animateColorAsState(targetValue = if (toggled) MaterialTheme.colors.secondaryVariant  else Color.Transparent, tween(300))

    AnimatedVisibility(
        visible = toggled,
        enter = expandHorizontally(tween(300)) { fullWidth -> 0 } + fadeIn(tween(200, 200)),
        exit = shrinkHorizontally(tween(250, 100)) { fullWidth -> 0 } + fadeOut(tween(150))
    ) { Spacer(modifier = Modifier.width(4.dp)) }
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!toggled) onSelected()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            AnimatedVisibility(
                visible = toggled,
                enter = expandHorizontally(tween(300)) { fullWidth -> 0 } + fadeIn(tween(200, 200)),
                exit = shrinkHorizontally(tween(250, 100)) { fullWidth -> 0 } + fadeOut(tween(150))
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .padding(start = 2.dp),
                    text = name.uppercase(),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
    AnimatedVisibility(
        visible = toggled,
        enter = expandHorizontally(tween(300)) { fullWidth -> 0 } + fadeIn(tween(200, 200)),
        exit = shrinkHorizontally(tween(250, 100)) { fullWidth -> 0 } + fadeOut(tween(150))
    ) { Spacer(modifier = Modifier.width(4.dp)) }
}