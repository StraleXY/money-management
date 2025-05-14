package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.BudgetCard
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.ReservedFund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.SavingsFund

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
        Column {
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
            Spacer(modifier = Modifier.height(12.dp))
            ReservedFund()
            Spacer(modifier = Modifier.height(12.dp))
            BudgetCard(
                colorsTest = vm.state.value.categories.filter { listOf("Food", "Glovo", "Market", "Pekara").contains(it.name) }.map { Colorer.getAdjustedDarkColor(it.color, MaterialTheme.colors.isLight) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SavingsFund()
        }
    }
}