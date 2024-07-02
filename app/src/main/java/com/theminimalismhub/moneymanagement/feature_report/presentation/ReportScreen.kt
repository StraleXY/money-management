package com.theminimalismhub.moneymanagement.feature_report.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryTotalsOverview
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard

@Destination(style = BaseTransition::class)
@Composable
fun ReportScreen(vm: ReportVM = hiltViewModel()) {

    val state = vm.state.value

    val TOP_PURCSHASES_COUNT = 10
    var showTopPurchasesAmount by remember { mutableStateOf(5) }
    fun toggleShowAmount() {
        showTopPurchasesAmount = if(showTopPurchasesAmount < TOP_PURCSHASES_COUNT) TOP_PURCSHASES_COUNT else 5
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .padding(top = 64.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${Currencier.formatAmount(state.total)} ${state.currency}",
                        style = MaterialTheme.typography.h1
                    )
                    Text(
                        text = "Total spent in ${state.year}.",
                        style = MaterialTheme.typography.h5
                    )
                }
                Row {
                    IconButton(onClick = { vm.onEvent(ReportEvent.SwitchYear(-1)) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = Icons.Default.ArrowBackIosNew.name
                        )
                    }
                    IconButton(onClick = { vm.onEvent(ReportEvent.SwitchYear(1)) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = Icons.Default.ArrowForwardIos.name
                        )
                    }
                }
            }
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

            Box(modifier = Modifier.animateContentSize()) {
                Column(modifier = Modifier.animateContentSize()) {
                    state.results.take(showTopPurchasesAmount).forEach { //.filter { it.category?.categoryId == 2 }
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
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.padding(end = 24.dp),
                    onClick = { toggleShowAmount() }
                ) {
                    Text(
                        text = if(showTopPurchasesAmount < TOP_PURCSHASES_COUNT) "SHOW ALL" else "COLLAPSE",
                        style = MaterialTheme.typography.button
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}