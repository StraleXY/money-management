package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
import android.icu.text.ListFormatter.Width
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.destinations.ManageAccountsScreenDestination
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddNewAccount
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.*
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
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

    BackHandler(enabled = state.isAddEditOpen) {
        vm.onEvent(HomeEvent.ToggleAddEditCard(null))
    }

    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        peekHeight = 0.dp,
        backLayerBackgroundColor = MaterialTheme.colors.background,
        frontLayerScrimColor = MaterialTheme.colors.surface.copy(
            0.8f,0.05f,0.05f, 0.05f
        ),
        frontLayerElevation = 2.dp,
        gesturesEnabled = !state.isAddEditOpen,
        appBar = {
            ScreenHeader(
                title = "Money Management",
                hint = "Take your finances into your hands!"
            )
        },
        backLayerContent = {
            // BACKDROP CONTENT
            MainAppActions(navigator)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(state.accounts.filter { it.active }) { account ->
                    AccountCardLarge(
                        account = account,
                        totalPerCategory = state.totalsPerAccount[account.accountId] ?: emptyList(),
                        maxAmount = if(!state.totalsPerAccount.containsKey(account.accountId) || state.totalsPerAccount[account.accountId]!!.isEmpty()) 0.0 else state.totalsPerAccount[account.accountId]!!.maxOf { it.amount }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                item {
                    AddNewAccount { navigator.navigate(ManageAccountsScreenDestination(isAddNew = true))}
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = { CancelableFAB(isExpanded = state.isAddEditOpen) { vm.onEvent(HomeEvent.ToggleAddEditCard(null)) } },
                scaffoldState = scaffoldState,
            ) {
                // HOME CONTENT
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(36.dp))
                            RangePicker(
                                rangeService = vm.rangeService,
                                rangePicked = { vm.onEvent(HomeEvent.RangeChanged(it)) }
                            )
                            QuickSpendingOverview(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                amount = state.results.sumOf { finance -> if(finance.finance.type == FinanceType.OUTCOME && finance.finance.trackable) finance.finance.amount else 0.0 },
                                rangeLength = vm.rangeService.rangeLength,
                                limit = 1000.0
                            )
                            ItemsTypeSelector(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                itemsTypeStates = state.itemsTypeStates,
                                itemToggled = { idx -> vm.onEvent(HomeEvent.ItemTypeSelected(idx)) }
                            )
                            AnimatedVisibility(
                                visible = vm.rangeService.rangeLength > 1,
                                enter = expandVertically(tween(400))
                                        + scaleIn(initialScale = 0.9f, animationSpec = tween(300, 450))
                                        + fadeIn(tween(300, 450)),
                                exit = scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                                        + fadeOut(tween(300))
                                        + shrinkVertically(tween(450, 250))
                            ) {
                                GraphSpendingOverview(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp),
                                    earningsPerTimePeriod = state.earningsPerTimePeriod,
                                    maxEarnings = state.maxEarnings
                                )
                            }
                            CategoryTotalsOverview(
                                totalPerCategory = state.totalPerCategory,
                                categoryBarStates = state.categoryBarStates
                            ) { vm.onEvent(HomeEvent.CategoryClicked(it)) }
                        }
                        items(state.results) {
                            FinanceCard(
                                finance = it,
                                previousSegmentDate = state.results.getOrNull(state.results.indexOf(it) - 1)?.getDay(),
                                onEdit = { vm.onEvent(HomeEvent.ToggleAddEditCard(it)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
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

@Composable
private fun ItemsTypeSelector(
    modifier: Modifier = Modifier,
    itemsTypeStates: Map<Int, MutableState<Boolean>>,
    itemToggled: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.surface.copy(
            red = 0.1f, green = 0.1f, blue = 0.1f
        ),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ToggleChip(
                name = "MIXED VIEW",
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_mobiledata_off_24),
                        contentDescription = "MIXED VIEW"
                    )
                },
                toggled = itemsTypeStates[0]!!.value,
                onSelected = { itemToggled(0) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "OUTCOME ITEMS",
                icon = {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "OUTCOME ITEMS"
                    )
                },
                toggled = itemsTypeStates[1]!!.value,
                onSelected = { itemToggled(1) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "INCOME ITEMS",
                icon = {
                    Icon(
                        modifier = Modifier.rotate(180f),
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "INCOME ITEMS"
                    )
                },
                toggled = itemsTypeStates[2]!!.value,
                onSelected = { itemToggled(2) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ToggleChip(
                name = "UNTRACKED ITEMS",
                icon = {
                    Icon(
                        imageVector = Icons.Default.MobiledataOff,
                        contentDescription = "UNTRACKED ITEMS"
                    )
                },
                toggled = itemsTypeStates[3]!!.value,
                onSelected = { itemToggled(3) }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}