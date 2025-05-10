package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddEditAccountCard
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.TransactionCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.SpendingSegment
import kotlin.math.roundToInt

enum class PanelState {
    Collapsed,
    Expanded
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    isAddNew: Boolean = false,
    vm: ManageAccountsViewModel = hiltViewModel()
) {
    val density = LocalDensity.current
    val pageHeight = with(LocalDensity.current) { LocalView.current.height.toDp() }
    val pagerHeight = 240.dp
    val headerHeight = 100.dp
    val accountButtonsHeight = 140.dp
    fun topSectionHeight() : Dp = headerHeight + pagerHeight + accountButtonsHeight
    val height = pageHeight - pagerHeight
    val swipeState = rememberSwipeableState(initialValue = PanelState.Expanded)
    val anchors = with(LocalDensity.current) { mapOf(topSectionHeight().toPx() to PanelState.Expanded, pagerHeight.toPx() to PanelState.Collapsed) }

    fun calcHeaderOffset(offset: Int): Int {
        var final: Int = 0
        with(density) {
            val fraction = (offset - topSectionHeight().toPx()) / (pagerHeight.toPx() - topSectionHeight().toPx())
            Log.d("Accounts", "Fraction: $fraction")
            final = (headerHeight * fraction).toPx().roundToInt()
        }
        return final
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.offset { IntOffset(x = 0, y = -calcHeaderOffset(swipeState.offset.value.roundToInt())) }
        ) {
            Header()
            AccountsPager()
            AccountButtons()
        }
        Box(
            Modifier
                .offset { IntOffset(x = 0, y = swipeState.offset.value.roundToInt()) }
                .fillMaxWidth()
                .height(height)
                .background(Color.DarkGray)
                .swipeable(
                    state = swipeState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
        ) {
//            LazyColumn {
//                item {
//                    AccountStats()
//                }
//                items(20) {
//                    Transaction()
//                }
//            }
            Text(
                "Swipeable Panel",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                color = Color.White
            )
        }
    }


}

@Composable
fun Header() {
    PlaceholderItem(100.dp, Color.Gray,"Header")
}

@Composable
fun AccountsPager() {
    PlaceholderItem(240.dp, Color.White, "Accounts")
}

@Composable
fun AccountButtons() {
    PlaceholderItem(140.dp, Color.Gray, "Account Buttons")
}

@Composable
fun AccountStats() {
    PlaceholderItem(140.dp, Color.DarkGray, "Account Stats")
}

@Composable
fun Transaction() {
    PlaceholderItem(50.dp, Color.Black, "Transaction")
}

@Composable
fun PlaceholderItem(
    height: Dp,
    color: Color,
    text: String
) {
    Column(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(color),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = text,
            style = MaterialTheme.typography.h4,
            color = if(color == Color.White) Color.Black else Color.White
        )
    }
}

//// OLD
@Composable
@OptIn(ExperimentalPagerApi::class)
fun ManageAccountsScreenOLD(
    isAddNew: Boolean = false,
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val scaffoldState = rememberScaffoldState()

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size,
        initialOffscreenLimit = 2,
    )

    val density = LocalDensity.current.density
    val root = LocalView.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    var accountsPagerHeight by remember { mutableStateOf(0.dp) }
    val screenHeight by remember { mutableStateOf(Dp(root.height / density)) }
    val scroll: ScrollState = rememberScrollState(0)

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
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .onSizeChanged { headerHeight = Dp(it.height / density) }
                            .graphicsLayer {
                                translationY = -(scroll.value.toFloat() * 1.2f)
                                    .coerceAtLeast(0f)
                                    .coerceAtMost(564f)
                            }
                    ) {
                        ScreenHeader(
                            title = "Manage Accounts",
                            hint = "Track your balance across multiple accounts!"
                        )
                        AccountsPager(
                            modifier = Modifier.onSizeChanged { accountsPagerHeight = Dp(it.height / density) },
                            accounts = state.accounts,
                            pagerState = pagerState,
                            currency = state.currency,
                            onAccountSelected = { if(state.accounts.size > it) vm.onEvent(ManageAccountsEvent.CardSelected(state.accounts[it])) }
                        )
                        AccountActions(
                            enabled = !pagerState.isScrollInProgress,
                            account = state.selectedAccount,
                            onToggleActivate = { vm.onEvent(ManageAccountsEvent.ToggleActive) },
                            onToggleEdit = { vm.onEvent(ManageAccountsEvent.ToggleAddEdit(state.selectedAccount)) },
                            onSetPrimary = { vm.onEvent(ManageAccountsEvent.PrimarySelected) },
                            onTransaction = { vm.onEvent(ManageAccountsEvent.ToggleTransaction) }
                        )
                    }
                }
            }
            SlidingCard(
                headerHeight = headerHeight,
                accountsPagerHeight = accountsPagerHeight,
                screenHeight = screenHeight,
                scrollState = scroll
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                if (state.results.isEmpty()) ErrorNoData()
                else {
                    AccountStats(
                        income = state.results.filter { it.finance.type == FinanceType.INCOME && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                        outcome = state.results.filter { it.finance.type == FinanceType.OUTCOME && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                        inTransaction = state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountId == state.selectedAccountId }.sumOf { it.finance.amount },
                        outTransaction = state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountIdFrom == state.selectedAccountId }.sumOf { it.finance.amount },
                        currency = state.currency
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                state.results.filter { it.finance.type == FinanceType.TRANSACTION && it.finance.financeAccountId == state.selectedAccountId }.forEach {
                    FinanceCard(
                        finance = it,
                        previousSegmentDate = state.results.getOrNull(state.results.indexOf(it) - 1)?.getDay(),
                        currency = state.currency,
                        onEdit = { }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                //TODO OPTIMIZE :)
            }

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
}

@Composable
fun SlidingCard(
    headerHeight: Dp,
    accountsPagerHeight: Dp,
    screenHeight: Dp,
    scrollState: ScrollState,
    content: @Composable ColumnScope.() -> Unit
) {

    LaunchedEffect(scrollState.isScrollInProgress && scrollState.value.dp > 5.dp && scrollState.value < (headerHeight - accountsPagerHeight).value ) {
        if (scrollState.value.dp < 150.dp) scrollState.animateScrollTo(0, tween(175))
        else if (scrollState.value < (headerHeight - accountsPagerHeight + 64.dp).value) scrollState.animateScrollTo((headerHeight - accountsPagerHeight + 65.dp).value.toInt(), tween(175))
    }

    var textTrans by remember { mutableStateOf(0f) }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(accountsPagerHeight))
        // CARD
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .graphicsLayer {
                    translationY =
                        ((headerHeight - accountsPagerHeight - scrollState.value.dp).toPx()).coerceAtLeast(0f)
                }
                .verticalScroll(scrollState)
        ) {
            // CONTENT CONTAINER - translates content to the top and then keeps it there
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .graphicsLayer {
                        val cardTranslationY = ((headerHeight - accountsPagerHeight - scrollState.value.dp).toPx()).coerceAtLeast(0f)
                        textTrans = if (cardTranslationY > 0) scrollState.value.dp.toPx() / 2 else textTrans
                        translationY = textTrans
                    }
            ) {
                // CONTENT - adds bottom podding
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .shadow(8.dp)
                        .heightIn(screenHeight - accountsPagerHeight + 160.dp)
                        .background(MaterialTheme.colors.surface)
                ) {
                    content()
                    Spacer(modifier = Modifier.height(160.dp))
                }
            }
        }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 16.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
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

