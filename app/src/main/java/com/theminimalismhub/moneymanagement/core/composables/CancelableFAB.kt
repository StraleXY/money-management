package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.R

@Composable
fun CancelableFAB(
    isExpanded: Boolean,
    onClick: () -> Unit
) {

    val contentColor = animateColorAsState(
        targetValue = if(isExpanded) Color.Transparent else MaterialTheme.colors.onPrimary,
        tween(150)
    )
    val animatedElevation = animateDpAsState(
        targetValue = if (isExpanded) 0.dp else 6.dp,
        animationSpec = tween(durationMillis = 250)
    )

    ExtendedFloatingActionButton(
        onClick = { onClick() },
        text = {
            Text(
                text = "ADD",
                style = MaterialTheme.typography.button,
                color = contentColor.value
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.hs_new_finance),
                tint = contentColor.value,
                modifier = Modifier.size(26.dp)
            )
        },
        modifier = Modifier
            .height(
                animateDpAsState(
                    targetValue = if (isExpanded) 45.dp else 56.dp,
                    tween(375)
                ).value
            )
            .padding(
                end = animateDpAsState(
                    targetValue = if (isExpanded) 17.dp else 0.dp,
                    tween(375)
                ).value
            )
            .alpha(
                animateFloatAsState(
                    targetValue = if(isExpanded) 0f else 1f,
                    tween(275, 275)
                ).value
            )
            .scale(
                animateFloatAsState(
                    targetValue = if(isExpanded) 0f else 1f,
                    tween(350, 300)
                ).value
            ),
        containerColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(64.dp),
        expanded = isExpanded,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = animatedElevation.value,
            pressedElevation = animatedElevation.value,
            focusedElevation = animatedElevation.value,
            hoveredElevation = animatedElevation.value
        )
    )
}