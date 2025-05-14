package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.DisplayFundCard

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
@Composable
fun ManageFundsScreen(
    vm: ManageFundsViewModel = hiltViewModel()
) {

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = false)  {
            // TODO Fab Click
        }},
        scaffoldState = rememberScaffoldState()
    ) {
        LazyColumn {
            item {
                ScreenHeader(
                    title = "Manage Funds",
                    hint = "Use Funds to budget, save or reserve money!",
                    spacerHeight = 48.dp
                )

                Button(
                    modifier = Modifier.offset(x = 24.dp),
                    onClick = {vm.onEvent(ManageFundsEvent.SaveFund)}
                ) {
                    Text(
                        text = "ADD TEST FUND",
                        style = MaterialTheme.typography.button
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
            items(vm.state.value.funds) { fund ->
                DisplayFundCard(fund)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}