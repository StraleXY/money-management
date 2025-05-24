package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VerticalAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(tween(400))
                + scaleIn(initialScale = 0.9f, animationSpec = tween(300, 450))
                + fadeIn(tween(300, 450)),
        exit = scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                + fadeOut(tween(300))
                + shrinkVertically(tween(450, 250)),
        content = content
    )
}