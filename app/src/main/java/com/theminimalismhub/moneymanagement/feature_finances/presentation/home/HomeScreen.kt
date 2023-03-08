package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.*
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.AddEditFinanceEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.FinanceCard
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.RangePicker
import java.util.*

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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    HomeScreenContent(navigator = navigator)
                    RangePicker(
                        rangeService = vm.rangeService
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                typeToggled = { vm.onEvent(AddEditFinanceEvent.ToggleType) },
                categorySelected = { vm.onEvent(AddEditFinanceEvent.CategorySelected(it)) },
                addFinance = { vm.onEvent(AddEditFinanceEvent.AddFinance) },
                dateChanged = { vm.onEvent(AddEditFinanceEvent.DateChanged(it)) }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    navigator: DestinationsNavigator
) {
    ScreenHeader(
        title = "Money Manager",
        hint = ""
    )
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