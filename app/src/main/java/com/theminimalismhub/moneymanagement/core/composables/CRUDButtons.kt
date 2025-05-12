package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground

@Composable
fun CRUDButtons(
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    deleteEnabled: Boolean = true,
    saveEnabled: Boolean = true
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val actionContentColor = animateColorAsState(
        targetValue = if(visible) MaterialTheme.colors.onPrimary else Color.Transparent,
        tween(300, 350)
    )

    Row(
        modifier = Modifier
            .height(45.dp)
            .padding(horizontal = 33.dp)
            .fillMaxWidth()
            .shadedBackground(Shade.LIGHT, RoundedCornerShape(100)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(2.dp))
            TextButton(
                modifier = Modifier,
                onClick = onCancel,
                shape = RoundedCornerShape(100)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier
                            .size(26.dp)
                            .alpha(0.8f)
                            .rotate(45f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.action_cancel),
                        style = MaterialTheme.typography.button,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            if (deleteEnabled) HoldableActionButton(
                modifier = Modifier,
                text = stringResource(id = R.string.action_delete),
                icon = Icons.Default.Delete,
                textStyle = MaterialTheme.typography.button,
                duration = 2500,
                circleColor = Color.Transparent,
                alternatedColor = MaterialTheme.colors.error,
                iconColor = MaterialTheme.colors.onBackground,
                onHold = onDelete,
                enabled = deleteEnabled
            )
        }
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .alpha(
                    animateFloatAsState(
                        targetValue = if(visible) 1f else 0f,
                        tween(200, 200)
                    ).value
                )
                .scale(
                    animateFloatAsState(
                        targetValue = if(visible) 1f else 0.85f,
                        tween(200, 250)
                    ).value
                ),
            onClick = onSave,
            enabled = saveEnabled,
            shape = RoundedCornerShape(100)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(0.8f),
                    tint = actionContentColor.value
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.action_save),
                    style = MaterialTheme.typography.button,
                    color = actionContentColor.value
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}