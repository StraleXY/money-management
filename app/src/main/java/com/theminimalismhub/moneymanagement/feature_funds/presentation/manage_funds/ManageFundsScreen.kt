package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.effects.alphaClickEffect
import com.theminimalismhub.moneymanagement.core.effects.scaledClickEffect
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.AddEditFundCard
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
@Composable
fun ManageFundsScreen(
    vm: ManageFundsViewModel = hiltViewModel()
) {

    BackHandler(vm.state.value.isAddEditOpen) {
        vm.onEvent(ManageFundsEvent.ToggleAddEdit())
    }

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = vm.state.value.isAddEditOpen)  {
            vm.onEvent(ManageFundsEvent.ToggleAddEdit())
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
                Spacer(Modifier.height(24.dp))
            }
            items(vm.state.value.funds) { fund ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .scaledClickEffect()
                        .clickable {
                            vm.onEvent(ManageFundsEvent.ToggleAddEdit(fund))
                        }
                ) {
                    DisplayFundCard(
                        fund = fund
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        }
        TranslucentOverlay(vm.state.value.isAddEditOpen)
        AddEditFundCard(
            isOpen = vm.state.value.isAddEditOpen,
            isNew = vm.state.value.sFundItem == null,
            form = vm.addEditFormState,
            fundType = vm.state.value.sFundType,
            onTypeFundSelected = { vm.onEvent(ManageFundsEvent.SelectFundType(it)) },
            accounts = vm.state.value.accounts,
            selectedAccounts = vm.state.value.sAccounts,
            onAccountIdsSelected = { vm.onEvent(ManageFundsEvent.SelectAccounts(it)) },
            categories = vm.state.value.categories,
            selectedCategories = vm.state.value.sCategories,
            onCategoryIdsSelected = { vm.onEvent(ManageFundsEvent.SelectCategories(it)) },
            selectedRecurring = vm.state.value.sRecurring,
            onRecurringSelected = { vm.onEvent(ManageFundsEvent.SelectRecurring(it)) },
            requestCardToClose = { vm.onEvent(ManageFundsEvent.ToggleAddEdit(null)) },
            onSave = { vm.onEvent(ManageFundsEvent.SaveFund) }
        )
    }
}