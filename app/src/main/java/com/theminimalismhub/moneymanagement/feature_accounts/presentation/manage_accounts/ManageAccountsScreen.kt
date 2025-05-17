package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountsPageContainer
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddEditAccountCard
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.TransactionCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.SpendingSegment

enum class PanelState {
    Collapsed,
    Expanded
}
fun Float.toOffset() = Offset(0f, this)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    isAddNew: Boolean = false,
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val scaffoldState = rememberScaffoldState()

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size,
        initialOffscreenLimit = 2,
    )

    LaunchedEffect(isAddNew) {
        if(isAddNew) vm.onEvent(ManageAccountsEvent.ToggleAddEdit(null))
    }

    BackHandler(enabled = state.isAddEditOpen || state.isTransactionOpen) {
        if (state.isAddEditOpen) vm.onEvent(ManageAccountsEvent.ToggleAddEdit(null))
        else if (state.isTransactionOpen) vm.onEvent(ManageAccountsEvent.ToggleTransaction)
    }

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = state.isAddEditOpen || state.isTransactionOpen) {
            if (state.isTransactionOpen) vm.onEvent(ManageAccountsEvent.ToggleTransaction)
            else vm.onEvent(ManageAccountsEvent.ToggleAddEdit(null))
        } },
        scaffoldState = scaffoldState,
    ) {
        AccountsPageContainer(
            header = {
                ScreenHeader(
                    title = "Manage Accounts",
                    hint = "Track your balance across multiple accounts!",
                    spacerHeight = 0.dp
                )
            },
            accountsPager = {
                Spacer(modifier = Modifier.height(24.dp))
                AccountsPager(
                    accounts = state.accounts,
                    pagerState = pagerState,
                    currency = state.currency,
                    onAccountSelected = { if(state.accounts.size > it) vm.onEvent(ManageAccountsEvent.CardSelected(state.accounts[it])) }
                )
            },
            accountButtons = {
                AccountActions(
                    enabled = !pagerState.isScrollInProgress,
                    account = state.selectedAccount,
                    onToggleActivate = { vm.onEvent(ManageAccountsEvent.ToggleActive) },
                    onToggleEdit = { vm.onEvent(ManageAccountsEvent.ToggleAddEdit(state.selectedAccount)) },
                    onSetPrimary = { vm.onEvent(ManageAccountsEvent.PrimarySelected) },
                    onTransaction = { vm.onEvent(ManageAccountsEvent.ToggleTransaction) }
                )
            },
            accountStats = {
                AccountStats(
                    income = state.results.filter { it.finance.type == FinanceType.INCOME && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                    outcome = state.results.filter { it.finance.type == FinanceType.OUTCOME && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                    inTransaction = state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                    outTransaction = state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountIdFrom == state.selectedAccountId }.sumOf { it.finance.amount },
                    currency = state.currency
                )
                Spacer(modifier = Modifier.height(24.dp))
            },
            items = {
                state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountId == state.selectedAccountId }.forEach {
                    FinanceCard(
                        finance = it,
                        previousSegmentDate = state.results.getOrNull(state.results.indexOf(it) - 1)?.getDay(),
                        currency = state.currency,
                        onEdit = { }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )

        TranslucentOverlay(visible = state.isAddEditOpen ||state.isTransactionOpen)
        AddEditAccountCard(
            isOpen = state.isAddEditOpen,
            type = state.currentType,
            form = vm.addEditFormState,
            currency = state.currency,
            accountTypeStates = state.accountTypeStates,
            isNew = state.selectedAccountId == null,
            onTypeChanged = { vm.onEvent(ManageAccountsEvent.TypeChanged(it)) },
            onSave = {
                if(!vm.addEditFormState.validate()) return@AddEditAccountCard
                vm.onEvent(ManageAccountsEvent.SaveAccount)
            },
            onDelete = { vm.onEvent(ManageAccountsEvent.DeleteAccount) },
            onCancel = { vm.onEvent(ManageAccountsEvent.ToggleAddEdit(null)) }
        )
        TransactionCard(
            isOpen = state.isTransactionOpen,
            form = vm.transactionFormState,
            accountFrom = state.selectedAccount,
            currency = state.currency,
            accounts = state.accounts.filter { account -> account.accountId != state.selectedAccount?.accountId },
            onTransaction = {
                if(!vm.transactionFormState.validate()) return@TransactionCard
                vm.onEvent(ManageAccountsEvent.ConfirmTransaction(it))
            },
            onCancel = { vm.onEvent(ManageAccountsEvent.ToggleTransaction)}
        )
    }

}

@Composable
fun AccountStats(
    income: Double,
    outcome: Double,
    inTransaction: Double,
    outTransaction: Double,
    currency: String
) {
    Box(
        modifier = Modifier
            .shadedBackground(Shade.MID)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 32.dp)) {
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(120.dp),
                title = "TOTAL INFLOW",
                amount = inTransaction + income,
                currency = currency,
                secondaryAmounts = listOf(
                    Pair("Transactions", inTransaction),
                    Pair("Income", income)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(88.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(120.dp),
                title = "TOTAL OUTFLOW",
                amount = outTransaction + outcome,
                currency = currency,
                secondaryAmounts = listOf(
                    Pair("Transactions", outTransaction),
                    Pair("Outcome", outcome)
                )
            )
        }
    }
}

