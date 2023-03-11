package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
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


    val animatedAlpha = remember{ Animatable(1f) }
    LaunchedEffect(state.results) {
        animatedAlpha.snapTo(0f)
        animatedAlpha.animateTo(
            targetValue = 1f,
            animationSpec = keyframes {
                durationMillis = 400
                0f at 150 with FastOutSlowInEasing
                1f at 400
            }
        )
    }

    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        peekHeight = 0.dp,
        backLayerBackgroundColor = MaterialTheme.colors.background,
        frontLayerScrimColor = Color(ColorUtils.setAlphaComponent(MaterialTheme.colors.surface.toArgb(), (255 * 0.90).toInt())),
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
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(state.accounts) { account ->
                    AccountCardLarge(
                        account = account,
                        totalPerCategory = state.totalPerCategory,
                        maxAmount = if(state.totalPerCategory.isEmpty()) 0.0 else state.totalPerCategory.maxOf { it.amount }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = {
                    androidx.compose.material3.ExtendedFloatingActionButton(
                        onClick = { vm.onEvent(HomeEvent.ToggleAddEditCard(null)) },
                        text = {
                            Text(
                                text = stringResource(id = R.string.action_cancel),
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.hs_new_finance),
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier
                                    .size(26.dp)
                                    .rotate(
                                        animateFloatAsState(
                                            targetValue = if (state.isAddEditOpen) -45f else 0f,
                                            tween(400)
                                        ).value
                                    )
                            )
                        },
                        modifier = Modifier
                            .height(
                                animateDpAsState(
                                    targetValue = if (state.isAddEditOpen) 45.dp else 56.dp,
                                    tween(350)
                                ).value
                            )
                            .padding(
                                end = animateDpAsState(
                                    targetValue = if (state.isAddEditOpen) 17.dp else 0.dp,
                                    tween(350)
                                ).value
                            ),
                        containerColor = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(64.dp),
                        expanded = state.isAddEditOpen
                    )
                },
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
//                            Box(
//                                modifier = Modifier
//                                    .width(50.dp)
//                                    .height(4.dp)
//                                    .background(
//                                        MaterialTheme.colors.primaryVariant,
//                                        RoundedCornerShape(100)
//                                    )
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
                            RangePicker(
                                rangeService = vm.rangeService,
                                rangePicked = { vm.onEvent(HomeEvent.RangeChanged(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            QuickSpendingOverview(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                amount = state.results.sumOf { finance -> if(finance.finance.type == FinanceType.OUTCOME) finance.finance.amount else 0.0 },
                                rangeLength = vm.rangeService.rangeLength,
                                limit = 1000.0
                            )
                            Spacer(modifier = Modifier.height(12.dp))
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
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(state.results) {
                            FinanceCard(
                                modifier = Modifier.alpha(animatedAlpha.value),
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
                        typeToggled = { vm.onEvent(AddEditFinanceEvent.ToggleType) },
                        categorySelected = { vm.onEvent(AddEditFinanceEvent.CategorySelected(it)) },
                        addFinance = { vm.onEvent(AddEditFinanceEvent.AddFinance) },
                        deleteFinance = { vm.onEvent(AddEditFinanceEvent.DeleteFinance)},
                        dateChanged = { vm.onEvent(AddEditFinanceEvent.DateChanged(it)) }
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
}