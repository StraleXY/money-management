package com.theminimalismhub.moneymanagement.feature_report.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryTotalsOverview
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeEvent

@Destination(style = BaseTransition::class)
@Composable
fun ReportScreen(vm: ReportVM = hiltViewModel()) {

    val state = vm.state.value

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            Text(
                text = "${Currencier.formatAmount(state.total)} ${state.currency}",
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .padding(top = 64.dp, bottom = 2.dp)
            )
            Text(
                text = "Total spent in ${state.year}.",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.65f)
                    .padding(horizontal = 27.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            CategoryTotalsOverview(
                totalPerCategory = state.totalPerCategory,
                categoryBarStates = state.categoryBarStates,
                currency = state.currency,
                collapsable = true,
                showCount = true
            ) { }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "10 Biggest Purchases",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.65f)
                    .padding(horizontal = 27.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(state.results.take(10)) {
            FinanceCard(
                modifier = Modifier.padding(horizontal = 4.dp),
                finance = it,
                previousSegmentDate = state.results.getOrNull(state.results.indexOf(it) - 1)?.getDay(),
                currency = state.currency,
                showSeparator = false,
                onEdit = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}