package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ErrorNoData
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddEditAccountCard
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.TransactionCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeEvent

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
                                    .coerceAtMost(430f)
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
                if(state.results.isEmpty()) ErrorNoData()
                state.results.forEach {
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
                onTypeChanged = { vm.onEvent(ManageAccountsEvent.TypeChanged(it)) },
                onSave = {
                    if(!vm.addEditFormState.validate()) return@AddEditAccountCard
                    vm.onEvent(ManageAccountsEvent.SaveAccount)
                },
                onDelete = { vm.onEvent(ManageAccountsEvent.DeleteAccount) }
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
                }
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
                .clip(RoundedCornerShape(15.dp))
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
                    .clip(RoundedCornerShape(15.dp))
                    .graphicsLayer {
                        val cardTranslationY = ((headerHeight - accountsPagerHeight - scrollState.value.dp).toPx()).coerceAtLeast(0f)
                        textTrans = if (cardTranslationY > 0) scrollState.value.dp.toPx() / 2 else textTrans
                        translationY = textTrans
                    }
            ) {
                // CONTENT - adds bottom podding
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .shadow(8.dp)
                        .heightIn(screenHeight - accountsPagerHeight + 160.dp)
                        .background(Color(0XFF212121))
                ) {
                    content()
                    Spacer(modifier = Modifier.height(160.dp))
                }
            }
        }
    }
}

