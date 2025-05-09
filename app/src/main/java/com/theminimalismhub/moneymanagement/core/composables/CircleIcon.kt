package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CircleIcon(
    icon: ImageVector
) {
    Circle {
        Icon(
            imageVector = icon,
            contentDescription = "Circle Icon",
            tint = MaterialTheme.colors.background,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable fun Circle(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .offset(y = (1.5).dp)
            .alpha(0.85f)
            .border(
                (1.5).dp,
                MaterialTheme.colors.background,
                RoundedCornerShape(19.dp)
            )
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
        content = content
    )
}