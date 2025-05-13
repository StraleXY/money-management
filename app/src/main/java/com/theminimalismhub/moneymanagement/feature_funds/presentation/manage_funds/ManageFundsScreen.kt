package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.ReservedFund

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
@Composable
fun ManageFundsScreen(

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

            ReservedFund()

        }
    }
}