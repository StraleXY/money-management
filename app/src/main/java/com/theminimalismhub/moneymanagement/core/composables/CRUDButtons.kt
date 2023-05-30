package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.R

@Composable
fun CRUDButtons(
    onSave: () -> Unit,
    onDelete: () -> Unit,
    deleteEnabled: Boolean = true,
    saveEnabled: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HoldableActionButton(
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
        Spacer(modifier = Modifier.width(12.dp))
        ActionChip(
            text = stringResource(id = R.string.action_save),
            icon = Icons.Default.Save,
            textStyle = MaterialTheme.typography.button,
            borderThickness = 0.dp,
            backgroundStrength = 0f,
            modifier = Modifier,
            onClick = onSave,
            enabled = saveEnabled
        )
    }
}