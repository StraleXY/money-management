package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import androidx.compose.material3.FloatingActionButtonDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import androidx.compose.material3.ExtendedFloatingActionButton as ExtendedFloatingActionButton3

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RootNavGraph(start = true)
@Destination
@Composable
fun ManageCategoriesScreen(
    navigator: DestinationsNavigator,
    vm: ManageCategoriesViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }
    val chipsHeight = remember { mutableStateOf(0f) }
    val headerHeight = remember { mutableStateOf(0f) }
    val animate = remember { mutableStateOf(false) }

    BackHandler(enabled = state.isAddEditOpen) {
        vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(null))
    }

    suspend fun animateTo(angel: Float) {
        rotation.animateTo(
            targetValue = 0f,
            animationSpec = keyframes {
                durationMillis = 300
                0f at 0
                angel at 150
                0f at 300
            }
        ) { currentRotation = value }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton3(
                onClick = { vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(null)) },
                text = {
                    Text(
                        text = "CANCEL",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new category",
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
                            tween(400)
                        ).value
                    ),
                containerColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(64.dp),
                expanded = state.isAddEditOpen
            )
        },
        scaffoldState = scaffoldState,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScreenHeader(
                    title = stringResource(id = R.string.category_screen_title),
                    hint = stringResource(id = R.string.category_screen_hint),
                    modifier = Modifier.onSizeChanged { headerHeight.value = it.height.toFloat() }
                )
                Spacer(modifier = Modifier.height(
                    animateDpAsState(
                        targetValue = if (state.isAddEditOpen) 24.dp else
                            Dp(((LocalView.current.height - chipsHeight.value - headerHeight.value) / LocalDensity.current.density) / 2) - 32.dp,
                        tween(if (state.isAddEditOpen) 250 else 350)
                    ).value)
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .onSizeChanged {
                            if (it.height > 0) chipsHeight.value = it.height.toFloat()
                        }
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    state.categories.forEach { category ->
                        ActionChip(
                            text = category.name,
                            textColor = Color(category.color),
                            backgroundStrength = 0.15f,
                            borderThickness = 0.dp,
                            onClick = { vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(category)) }
                        )
                    }
                }
            }
        }
    }
}