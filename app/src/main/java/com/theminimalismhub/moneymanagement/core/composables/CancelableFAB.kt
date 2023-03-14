package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.R

@Composable
fun CancelableFAB(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        text = {
            Text(
                text = stringResource(id = R.string.action_cancel),
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.hs_new_finance),
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(26.dp)
                    .rotate(
                        animateFloatAsState(
                            targetValue = if (isExpanded) -45f else 0f,
                            tween(400)
                        ).value
                    )
            )
        },
        modifier = Modifier
            .height(
                animateDpAsState(
                    targetValue = if (isExpanded) 45.dp else 56.dp,
                    tween(350)
                ).value
            )
            .padding(
                end = animateDpAsState(
                    targetValue = if (isExpanded) 17.dp else 0.dp,
                    tween(350)
                ).value
            ),
        containerColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(64.dp),
        expanded = isExpanded
    )
}