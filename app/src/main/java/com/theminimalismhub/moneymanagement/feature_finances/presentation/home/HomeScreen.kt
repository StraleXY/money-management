package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.destinations.ManageCategoriesScreenDestination
import com.theminimalismhub.moneymanagement.destinations.SettingsScreenDestination

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RootNavGraph(start = true)
@Destination(style = BaseTransition::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                backgroundColor = MaterialTheme.colors.primary
            ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Add new job") }
        },
        scaffoldState = scaffoldState,
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
        }
    }
}