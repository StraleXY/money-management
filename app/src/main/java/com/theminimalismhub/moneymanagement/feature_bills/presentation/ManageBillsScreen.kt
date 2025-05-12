package com.theminimalismhub.moneymanagement.feature_bills.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.DashedLine
import com.theminimalismhub.moneymanagement.core.composables.ErrorBox
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.HoldableActionButton
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill.AddEditBillCard
import com.theminimalismhub.moneymanagement.feature_bills.presentation.composables.BillCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsList
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.SpendingSegment
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.SwipeableAccountsPager
import com.theminimalismhub.moneymanagement.feature_settings.composables.SettingsSegment

@OptIn(ExperimentalPagerApi::class)
@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(style = BaseTransition::class)
fun ManageBillsScreen(vm: ManageBillsViewModel = hiltViewModel()) {

    val state = vm.state.value
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val amount: TextFieldState = vm.paymentFormState.getState("amount")
    val accountPagerState = rememberPagerState(
        pageCount = state.accounts.filter { it.active }.size,
        initialOffscreenLimit = 2,
    )

    BackHandler(enabled = state.isAddEditOpen || state.billToPay != null) {
        if(state.isAddEditOpen) vm.onEvent(ManageBillsEvent.ToggleAddEdit(null))
        vm.onEvent(ManageBillsEvent.TogglePayBill(null))
    }

    LaunchedEffect(state.paymentAccountId) {
        if(state.paymentAccountId == null || state.accounts.isEmpty()) return@LaunchedEffect
        accountPagerState.scrollToPage(state.accounts.filter { it.active }.indexOf(state.accounts.filter { it.active }.first { it.accountId == state.paymentAccountId } ))
    }

    Scaffold(
        floatingActionButton = { CancelableFAB(isExpanded = state.isAddEditOpen || state.billToPay != null) {
            if(state.billToPay == null) vm.onEvent(ManageBillsEvent.ToggleAddEdit(null))
            vm.onEvent(ManageBillsEvent.TogglePayBill(null))
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
                    if(state.bills.isNotEmpty()) Box(
                        modifier = Modifier
                            .padding(bottom = 28.dp)
                            .shadedBackground(Shade.DARK)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 24.dp, bottom = 24.dp)
                        ) {
                            SpendingSegment(
                                modifier = Modifier
                                    .weight(0.49f, true)
                                    .height(125.dp),
                                title = "PAID",
                                amount = state.bills.count { it.bill.isLastMonthPaid }.toDouble(),
                                currency = "/ ${state.bills.size}"
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(124.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            SpendingSegment(
                                modifier = Modifier
                                    .weight(0.49f, true)
                                    .height(125.dp),
                                title = "OWED",
                                amount = state.bills.sumOf { if (!it.bill.isLastMonthPaid) it.bill.amount else 0.0 }.toDouble(),
                                currency = state.currency
                            )
                        }
                    }
                    if(state.bills.isEmpty()) ErrorBox(
                        modifier = Modifier.padding(horizontal = 22.dp),
                        text = "No Bills",
                        hint = "Bills are used as a shortcut for reoccurring payments."
                    )
                }
                items(state.bills.filter { !it.bill.isLastMonthPaid }) {
                    BillCard(
                        bill = it,
                        onEdit = { vm.onEvent(ManageBillsEvent.ToggleAddEdit(it)) },
                        onPay = { vm.onEvent(ManageBillsEvent.TogglePayBill(it)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    if(state.bills.any { it.bill.isLastMonthPaid }) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SettingsSegment(name = "DUE NEXT MONTH")
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                items(state.bills.filter { it.bill.isLastMonthPaid }) {
                    BillCard(
                        bill = it,
                        onEdit = { vm.onEvent(ManageBillsEvent.ToggleAddEdit(it)) },
                        onPay = { vm.onEvent(ManageBillsEvent.TogglePayBill(it)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            TranslucentOverlay(visible = state.isAddEditOpen || state.billToPay != null)
            AddEditBillCard(
                isOpen = state.isAddEditOpen,
                vm = vm.addEditBillVM,
                onCancel = { vm.onEvent(ManageBillsEvent.ToggleAddEdit(null)) }
            )

            FloatingCard(
                visible = state.billToPay != null,
                header = {
                    SwipeableAccountsPager(
                        accounts = state.accounts.filter { it.active },
                        currency = state.currency,
                        balanceDelta = 0.0,
                        pagerState = accountPagerState,
                        minAlpha = 0.5f,
                        initialCardScale = 1.025f,
                        selectedCardStartScale = 0.875f,
                        selectedCardScale = 1.085f,
                        cardSpacing = 0.dp,
                        onAccountSelected = { idx -> vm.onEvent(ManageBillsEvent.PaymentAccountSelected(state.accounts.filter { it.active }[idx].accountId!!)) }
                    )
                    DashedLine(
                        modifier = Modifier
                            .offset(y = 17.dp)
                            .zIndex(100f),
                        dashLength = 8.dp,
                        gapLength = 4.dp
                    )
                }
            ) {
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.change(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 36.dp),
                    textStyle = MaterialTheme.typography.body1,
                    label = { Text(text = "Amount") },
                    isError = amount.hasError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
                    shape = RoundedCornerShape(100)
                )
                ErrorText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    message = amount.errorMessage,
                    hasError = amount.hasError
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .height(47.dp)
                        .padding(horizontal = 33.dp)
                        .fillMaxWidth()
                        .shadedBackground(Shade.LIGHT, RoundedCornerShape(100)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(2.dp))
                        TextButton(
                            modifier = Modifier,
                            onClick = { vm.onEvent(ManageBillsEvent.TogglePayBill(null)) },
                            shape = RoundedCornerShape(100)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .alpha(0.8f)
                                        .rotate(45f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(id = R.string.action_cancel),
                                    style = MaterialTheme.typography.button,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.primary, RoundedCornerShape(100))
                    ) {
                        HoldableActionButton(
                            modifier = Modifier
                                .padding(start = 9.dp, end = 14.dp)
                                .align(Alignment.Center),
                            text = "CONFIRM PAYMENT",
                            icon = Icons.Default.AttachMoney,
                            textStyle = MaterialTheme.typography.button,
                            duration = 2000,
                            circleColor = Color.Transparent,
                            alternatedColor = MaterialTheme.colors.onPrimary,
                            iconColor = MaterialTheme.colors.onPrimary,
                            onHold = { vm.onEvent(ManageBillsEvent.PayBill) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height((9.5).dp))
            }
        }
    }

}