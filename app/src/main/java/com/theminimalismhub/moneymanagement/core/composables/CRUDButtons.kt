package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    deleteEnabled: Boolean = true,
    saveEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .height(47.dp)
            .padding(horizontal = 33.dp)
            .fillMaxWidth()
            .shadedBackground(Shade.LIGHT, RoundedCornerShape(100)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        TextButton(
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
                        .alpha(0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.action_save),
                    style = MaterialTheme.typography.button
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
//        ActionChip(
//            text = stringResource(id = R.string.action_save),
//            icon = Icons.Default.Save,
//            textStyle = MaterialTheme.typography.button,
//            borderThickness = 0.dp,
//            backgroundStrength = 0.1f,
//            modifier = Modifier,
//            onClick = onSave,
//            enabled = saveEnabled
//        )
        Spacer(modifier = Modifier.width(12.dp))
        if(deleteEnabled) HoldableActionButton(
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
}