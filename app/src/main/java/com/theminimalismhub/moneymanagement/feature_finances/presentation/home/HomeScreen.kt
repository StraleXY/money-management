package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.CircularTypeSelector
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ManageCategoriesEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip

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
    val focusManager = LocalFocusManager.current

    val name: TextFieldState = vm.formState.getState("name")
    val amount: TextFieldState = vm.formState.getState("amount")

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
            HomeScreenContainer {
                HomeScreenContent(navigator = navigator)
            }
            TranslucentOverlay(visible = state.isAddEditOpen)
            FloatingCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                visible = state.isAddEditOpen
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 34.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    item {
                        CircularTypeSelector(
                            selectedType = state.currentType,
                            backgroundColor = Color.Transparent,
                            borderStroke = BorderStroke(1.5.dp, MaterialTheme.run { colors.onSurface.copy(alpha = ContentAlpha.disabled) })
                        ) { vm.onEvent(HomeEvent.ToggleType) }
                        Spacer(modifier = Modifier.width(7.dp))
                    }
                    items(state.categories) { category ->
                        state.categoryStates[category.categoryId!!]?.let {
                            CategoryChip(
                                text = category.name,
                                color = Color(category.color),
                                isToggled = it.value,
                                onToggled = { vm.onEvent(HomeEvent.CategorySelected(category.categoryId)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = "07/03/2023",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    textStyle = MaterialTheme.typography.body2,
                    label = { Text(text = "Date") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.change(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    textStyle = MaterialTheme.typography.body2,
                    label = { Text(text = "Name") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.change(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    textStyle = MaterialTheme.typography.body2,
                    label = { Text(text = "Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) })
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HoldableActionButton(
                        modifier = Modifier,
                        text = stringResource(id = R.string.action_delete),
                        icon = Icons.Default.Delete,
                        textStyle = MaterialTheme.typography.button,
                        duration = 2500,
                        circleColor = Color.Transparent,
                        alternatedColor = MaterialTheme.colors.error,
                        iconColor = MaterialTheme.colors.onBackground,
                        onHold = {  }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    ActionChip(
                        text = stringResource(id = R.string.action_save),
                        icon = Icons.Default.Save,
                        textStyle = MaterialTheme.typography.button,
                        borderThickness = 0.dp,
                        backgroundStrength = 0f,
                        modifier = Modifier,
                        onClick = {  }
                    )
                }
//                TextButton(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .padding(end = 36.dp)
//                ) {
//                    Text(
//                        text = "SAVE FINANCE ENTRY",
//                        style = MaterialTheme.typography.button
//                    )
//                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun HomeScreenContainer(
    content: @Composable LazyItemScope.() -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ScreenHeader(
                title = "Money Manager",
                hint = ""
            )
            content()
        }
    }
}

@Composable
private fun HomeScreenContent(
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