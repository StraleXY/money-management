package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GraphSpendingOverview(
    modifier: Modifier = Modifier,
    earningsPerTimePeriod: List<GraphEntry>,
    maxEarnings: Double
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = 0.dp
    ) {
        if(earningsPerTimePeriod.isNotEmpty()) {
            Graph(
                modifier = Modifier
                    .padding(20.dp),
                earnings = earningsPerTimePeriod,
                maxVal = maxEarnings
            )
        }
    }
}