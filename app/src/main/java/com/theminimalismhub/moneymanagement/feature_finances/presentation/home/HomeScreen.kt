package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
import android.icu.text.ListFormatter.Width
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.enums.RangeType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.destinations.ManageAccountsScreenDestination
import com.theminimalismhub.moneymanagement.destinations.ManageBillsScreenDestination
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.ReportScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddNewAccount
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.AccountsPager
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ToggleTracking
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.*
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RootNavGraph(start = true)
@Destination(style = BaseTransition::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    vm: HomeViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val scaffoldState = rememberScaffoldState()
    vm.colors = MaterialTheme.colors

    BackHandler(enabled = state.isAddEditOpen) {
        vm.onEvent(HomeEvent.ToggleAddEditCard(null))
    }

    LaunchedEffect(Unit) { vm.init() }

    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        peekHeight = 0.dp,
        backLayerBackgroundColor = MaterialTheme.colors.background,
        frontLayerScrimColor =
            if(MaterialTheme.colors.isLight) Color(ColorUtils.setAlphaComponent(MaterialTheme.colors.secondaryVariant.toArgb(), (1 * 255L).toInt()))
            else Color(ColorUtils.setAlphaComponent(MaterialTheme.colors.surface.toArgb(), (1f * 255L).toInt())),
        frontLayerElevation = 8.dp,
        frontLayerShape = RoundedCornerShape(0),
        gesturesEnabled = !state.isAddEditOpen,
        appBar = {
            ScreenHeader(
                title = "Money Management",
                hint = "Take your finances into your hands!"
            )
        },
        backLayerContent = {
            // BACKDROP CONTENT
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(state.accounts.filter { it.active }) { account ->
                    AccountCardLarge(
                        account = account,
                        totalPerCategory = state.totalsPerAccount[account.accountId] ?: emptyList(),
                        maxAmount = if(!state.totalsPerAccount.containsKey(account.accountId) || state.totalsPerAccount[account.accountId]!!.isEmpty()) 0.0 else state.totalsPerAccount[account.accountId]!!.maxOf { it.amount },
                        currency = state.currency
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                item {
                    AddNewAccount { navigator.navigate(ManageAccountsScreenDestination(isAddNew = true))}
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            MainAppActions(navigator)
            Spacer(modifier = Modifier.height(12.dp))
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = { CancelableFAB(isExpanded = state.isAddEditOpen) { vm.onEvent(HomeEvent.ToggleAddEditCard(null)) } },
                scaffoldState = scaffoldState,
            ) {
                // HOME CONTENT
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 84.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
//                            Box(modifier = Modifier.height(32.dp).shadedBackground(Shade.LIGHT)) { }
                            if(!state.swipeableNavigation) {
                                RangePicker(
                                    rangeService = vm.rangeService,
                                    isToday = state.isToday
                                ) { range, today -> vm.onEvent(HomeEvent.RangeChanged(range, today)) }
                                QuickSpendingOverviewCompact(
                                    exampleDate = "Spent",
                                    amount = state.quickSpendingAmount,
                                    average = state.dailyAverage,
                                    rangeLength = vm.rangeService.rangeLength,
                                    limit = state.limit,
                                    limitHidden = !state.displayTypes.contains(FinanceType.OUTCOME),
                                    currency = state.currency,
                                    selectedCategory = state.totalPerCategory.find { it.categoryId == state.selectedCategoryId }
                                )
                            }
                            else CardRangePicker(
                                rangeService = vm.rangeService,
                                isToday = state.isToday,
                                rangePicked = { range, today -> vm.onEvent(HomeEvent.RangeChanged(range, today)) },
                                amount = state.quickSpendingAmount,
                                average = state.dailyAverage,
                                limit = state.limit,
                                limitHidden = !state.displayTypes.contains(FinanceType.OUTCOME),
                                currency = state.currency,
                                selectedCategory = state.totalPerCategory.find { it.categoryId == state.selectedCategoryId }
                            )
                            ItemTypeSelectorV2(
                                onTypeChanged = { vm.onEvent(HomeEvent.DisplayTypeChanged(it)) },
                                onTrackedChanged = { vm.onEvent(HomeEvent.DisplayTrackedChanged(it)) }
                            )
                            AnimatedVisibility(
                                visible = state.itemsTypeStates[2]!!.value && state.filterIncomeByAccount || state.itemsTypeStates.filter { it.key != 2 }.any { it.value.value } && state.filterOutcomeByAccount,
                                enter = expandVertically(tween(400))
                                        + scaleIn(initialScale = 0.9f, animationSpec = tween(300, 450))
                                        + fadeIn(tween(300, 450)),
                                exit = scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                                        + fadeOut(tween(300))
                                        + shrinkVertically(tween(450, 250))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadedBackground(Shade.DARK),
                                ) {
                                    AccountsChips(
                                        spacing = 8.dp,
                                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                                        accounts = state.accounts,
                                        states = state.accountStates
                                    ) { vm.onEvent(HomeEvent.AccountClicked(it)) }
                                }
                            }
                            AnimatedVisibility(
                                visible = vm.rangeService.rangeLength > 1 && state.showLineGraph,
                                enter = expandVertically(tween(400))
                                        + scaleIn(initialScale = 0.9f, animationSpec = tween(300, 450))
                                        + fadeIn(tween(300, 450)),
                                exit = scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                                        + fadeOut(tween(300))
                                        + shrinkVertically(tween(450, 250))
                            ) {
                                GraphSpendingOverview(
                                    earningsPerTimePeriod = state.earningsPerTimePeriod,
                                    maxEarnings = state.maxEarnings,
                                    limit = state.limit
                                )
                            }
                            CategoryTotalsOverview(
                                totalPerCategory = state.totalPerCategory,
                                categoryBarStates = state.categoryBarStates,
                                currency = state.currency,
                                collapsable = state.collapseCategories
                            ) { vm.onEvent(HomeEvent.CategoryClicked(it)) }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(state.results) {
                            FinanceCard(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                finance = it,
                                previousSegmentDate = state.results.getOrNull(state.results.indexOf(it) - 1)?.getDay(),
                                currency = state.currency,
                                onEdit = { vm.onEvent(HomeEvent.ToggleAddEditCard(it)) }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    TranslucentOverlay(visible = state.isAddEditOpen)
                    AddEditFinanceCard(
                        state = vm.addEditService.state.value,
                        isOpen = state.isAddEditOpen,
                        form = vm.addEditService.formState,
                        cardToggled = { vm.onEvent(HomeEvent.ToggleAddEditCard(it)) },
                        accountSelected = { vm.onEvent(AddEditFinanceEvent.AccountSelected(it))},
                        typeToggled = { vm.onEvent(AddEditFinanceEvent.ToggleType) },
                        categorySelected = { vm.onEvent(AddEditFinanceEvent.CategorySelected(it)) },
                        addFinance = { vm.onEvent(AddEditFinanceEvent.AddFinance) },
                        deleteFinance = { vm.onEvent(AddEditFinanceEvent.DeleteFinance)},
                        dateChanged = { vm.onEvent(AddEditFinanceEvent.DateChanged(it)) },
                        trackableToggled = { vm.onEvent(AddEditFinanceEvent.TrackableToggled) }
                    )
                }
            }
        }
    )
}

@Composable
private fun MainAppActions(
    navigator: DestinationsNavigator
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        item {
            ActionChip(
                text = "ACCOUNTS",
                icon = Icons.Default.AccountBalance,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 1.dp,
                accentColor = MaterialTheme.colors.primaryVariant,
                backgroundStrength = 0f,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                onClick = {
                    navigator.navigate(ManageAccountsScreenDestination())
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionChip(
                text = "BILLS",
                icon = Icons.Default.Receipt,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 1.dp,
                accentColor = MaterialTheme.colors.primaryVariant,
                backgroundStrength = 0f,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                onClick = {
                    navigator.navigate(ManageBillsScreenDestination())
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionChip(
                text = "YEARLY REPORT",
                icon = Icons.Default.BarChart,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 1.dp,
                accentColor = MaterialTheme.colors.primaryVariant,
                backgroundStrength = 0f,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                onClick = {
                    navigator.navigate(ReportScreenDestination())
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionChip(
                text = "CATEGORIES",
                icon = Icons.Default.Category,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 1.dp,
                accentColor = MaterialTheme.colors.primaryVariant,
                backgroundStrength = 0f,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                onClick = {
                    navigator.navigate(ManageCategoriesScreenDestination())
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionChip(
                text = "SETTINGS",
                icon = Icons.Default.Settings,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 1.dp,
                accentColor = MaterialTheme.colors.primaryVariant,
                backgroundStrength = 0f,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                onClick = {
                    navigator.navigate(SettingsScreenDestination())
                }
            )
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}