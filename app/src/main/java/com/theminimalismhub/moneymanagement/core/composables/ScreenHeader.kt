package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground

@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    title: String,
    spacerHeight: Dp = 24.dp,
    hint: String? = null
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .padding(top = 64.dp, bottom = 4.dp)
        )
        if (hint != null) {
            Text(
                text = hint,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.65f)
                    .padding(horizontal = 27.dp)
            )
            Spacer(modifier = Modifier.height(spacerHeight))
        }
    }
}