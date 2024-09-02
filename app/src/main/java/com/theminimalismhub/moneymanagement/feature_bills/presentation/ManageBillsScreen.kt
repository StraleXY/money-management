package com.theminimalismhub.moneymanagement.feature_bills.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.ManageAccountsEvent
import com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill.AddEditBillCard
import com.theminimalismhub.moneymanagement.feature_bills.presentation.composables.BillCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeEvent

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
fun ManageBillsScreen(vm: ManageBillsViewModel = hiltViewModel()) {

    val state = vm.state.value
    val scaffoldState = rememberScaffoldState()

    BackHandler(enabled = state.isAddEditOpen) {
        vm.onEvent(ManageBillsEvent.ToggleAddEdit(null))
    }

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = state.isAddEditOpen) {
            vm.onEvent(ManageBillsEvent.ToggleAddEdit(null))
        } },
        scaffoldState = scaffoldState,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ScreenHeader(
                        title = stringResource(id = R.string.b_title),
                        hint = stringResource(id = R.string.b_hint)
                    )
                }
                items(state.bills) {
                    BillCard(bill = it) { vm.onEvent(ManageBillsEvent.ToggleAddEdit(it)) }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            TranslucentOverlay(visible = state.isAddEditOpen)
            AddEditBillCard(
                isOpen = state.isAddEditOpen,
                vm = vm.addEditBillVM
            )
        }
    }

}