package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.math.MathUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.CancelableFAB
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.CircularActionButton
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.DashedCircularActionButton
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.AddEditFundCard
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard
import kotlin.math.absoluteValue

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
        LazyColumn(Modifier.shadedBackground(Shade.DARK)) {
            item {
                ScreenHeader(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    title = "Manage Funds",
                    hint = "Use Funds to budget, save or reserve money!",
                    spacerHeight = 12.dp
                )
                BudgetFundsPager(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    budgets = vm.state.value.funds.filter { it.item.type == FundType.BUDGET },
                    onFundSelected = { vm.onEvent(ManageFundsEvent.ToggleAddEdit(it)) }
                )
                Spacer(Modifier.height(24.dp))
            }
            items(vm.state.value.funds.filter { it.item.type != FundType.BUDGET }.sortedBy { it.item.type.value }) { fund ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
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
            onSave = { vm.onEvent(ManageFundsEvent.SaveFund) },
            onDelete = { vm.onEvent(ManageFundsEvent.DeleteFund) }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BudgetFundsPager(
    modifier: Modifier = Modifier,
    budgets: List<Fund>,
    minAlpha: Float = 1f,
    initialCardScale: Float = 1.05f,
    selectedCardScale: Float = 1.15f,
    selectedCardStartScale: Float = 0.95f,
    cardSpacing: Dp = 0.dp,
    onFundSelected: (Fund) -> Unit
) {

    val pagerState = rememberPagerState(budgets.size, 0)
    val width = with(LocalDensity.current) { LocalView.current.width.toDp() }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 40.dp, bottom = 40.dp)
    ) { idx ->
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(idx).absoluteValue

                    MathUtils
                        .lerp(selectedCardStartScale, selectedCardScale, 1f - pageOffset.coerceIn(0f, 1f))
                        .also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                    MathUtils
                        .lerp(minAlpha, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        .also { value -> alpha = value }
                }
                .padding(horizontal = cardSpacing)
        ) {
            Box(
                modifier = Modifier
                    .width(((width - 112.dp) * initialCardScale))
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onFundSelected(budgets[idx]) }
                        .scale(0.85f)
                ) {
                    if (budgets.size > idx) DisplayFundCard(
                        fund = budgets[idx]
                    )
                }
            }
        }
    }
}