package com.theminimalismhub.moneymanagement.core.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun ErrorNoData(
    modifier: Modifier = Modifier,
    text: String = "No Data",
    hint: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp)
    ) {
        Text(
            text = text.uppercase(Locale.ROOT),
            style = MaterialTheme.typography.body2
                .copy(
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp
                ),
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colors.secondary
        )
        if(hint != null) {
            Text(
                text = hint,
                style = MaterialTheme.typography.subtitle2
                    .copy(
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    ),
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    text: String = "No Data",
    hint: String? = null,
    strokeColor: Color = MaterialTheme.colors.secondary
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = strokeColor,
                    style = stroke,
                    cornerRadius = CornerRadius(15.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ) {
        ErrorNoData(
            modifier = Modifier
                .padding(vertical = 16.dp),
            text = text,
            hint = hint
        )
    }
}