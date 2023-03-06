package com.theminimalismhub.moneymanagement.feature_settings.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSegment(
    name: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = name,
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.subtitle2
                .copy(fontSize = 13.sp),
            modifier = Modifier.alpha(0.85f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Divider()
    }
}