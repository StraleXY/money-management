package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData

@Composable
fun GraphSpendingOverview(
    modifier: Modifier = Modifier,
    earningsPerTimePeriod: List<GraphEntry>,
    maxEarnings: Double,
    limit: Double
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .animateContentSize(),
        backgroundColor = MaterialTheme.colors.surface.copy(
            red = 0.1f, green = 0.1f, blue = 0.1f
        ),
        elevation = 4.dp
    ) {
        if(earningsPerTimePeriod.sumOf { it.value } > 0) {
            Graph(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                totalHeight = 160.dp,
                earnings = earningsPerTimePeriod,
                maxEarnings = maxEarnings,
                limit = limit
            )
        } else ErrorNoData(modifier = Modifier.padding(vertical = 20.dp))
    }
}