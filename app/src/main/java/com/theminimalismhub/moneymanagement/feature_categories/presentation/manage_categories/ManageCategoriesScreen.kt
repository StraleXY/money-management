package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HSVColor
import com.theminimalismhub.moneymanagement.core.composables.ColorWheel.HarmonyColorPicker
import com.theminimalismhub.moneymanagement.core.composables.FramelessInputField
import com.theminimalismhub.moneymanagement.core.composables.TranslucentOverlay
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
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
    val scaffoldState = rememberScaffoldState()

    val chipsHeight = remember { mutableStateOf(0f) }
    val headerHeight = remember { mutableStateOf(0f) }
    val animate = remember { mutableStateOf(false) }

    BackHandler(enabled = state.isAddEditOpen) {
        vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(null))
    }


    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton3(
                onClick = { vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(null)) },
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
                        contentDescription = stringResource(id = R.string.cs_new_category),
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
                    title = stringResource(id = R.string.cs_title),
                    hint = stringResource(id = R.string.cs_hint),
                    modifier = Modifier.onSizeChanged { headerHeight.value = it.height.toFloat() }
                )
                Spacer(modifier = Modifier.height(
                    animateDpAsState(
                        targetValue = if (state.isAddEditOpen) 24.dp else
                            Dp(((LocalView.current.height - chipsHeight.value - headerHeight.value) / LocalDensity.current.density) / 2) - 32.dp,
                        tween(if (state.isAddEditOpen) 250 else 350)
                    ).value)
                )
                CategoryContainer(
                    chipsHeight = chipsHeight,
                    categories = state.outcomeCategories,
                    onClick = { vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(it)) }
                )
                CategoryContainer(
                    chipsHeight = chipsHeight,
                    categories = state.incomeCategories,
                    onClick = { vm.onEvent(ManageCategoriesEvent.ToggleAddEditCard(it)) }
                )
            }
            TranslucentOverlay(visible = state.isAddEditOpen)
            FloatingCard(
                visible = state.isAddEditOpen
            ) {
                InputCategoryChip(
                    color = state.currentColor,
                    name = state.currentName,
                    onChanged = { vm.onEvent(ManageCategoriesEvent.EnteredName(it)) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                HarmonyColorPicker(
                    modifier = Modifier.size(250.dp),
                    color = state.currentColor,
                    isBrightnessFixed = false
                ) { color -> vm.onEvent(ManageCategoriesEvent.ColorChanged(color)) }
                Text(
                    modifier = Modifier
                        .clickable { vm.onEvent(ManageCategoriesEvent.SaveCategory) },
                    text = "SAVE",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryContainer(
    chipsHeight: MutableState<Float>,
    categories: List<Category>,
    onClick: (Category) -> Unit
) {
    val containerHeight = remember { mutableStateOf(0f) }
    FlowRow(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged {
                if (it.height == 0) return@onSizeChanged
                chipsHeight.value += (containerHeight.value - it.height.toFloat())
                containerHeight.value = it.height.toFloat()
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        categories.forEach { category ->
            ActionChip(
                text = category.name,
                textColor = Color(category.color),
                backgroundStrength = 0.15f,
                borderThickness = 0.dp,
                onClick = { onClick(category) }
            )
        }
    }
}

@Composable
private fun FloatingCard(
    visible: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 }),
        exit = fadeOut(tween(450))
                + slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight / 2 }, animationSpec = tween(450)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 58.dp)
                    .align(Alignment.BottomCenter),
                elevation = Dp(8f),
                shape = RoundedCornerShape(15.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )
            }
        }
    }
}

@Composable
private fun InputCategoryChip(
    modifier: Modifier = Modifier,
    color: HSVColor,
    name: String,
    enabled: Boolean = true,
    onChanged: (String) -> Unit
) {

    val scope = rememberCoroutineScope()
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

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

    Card(
        modifier = modifier
            .padding(5.dp)
            .height(38.dp)
            .graphicsLayer {
                rotationY = currentRotation
            },
        elevation = Dp(8f),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color(
            ColorUtils.setAlphaComponent(
                color.toColor().toArgb(),
                (0.1f * 255L).roundToInt()
            )
        )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
            FramelessInputField(
                text = name,
                textColor = color.toColor(),
                characterLimit = 30,
                hint = stringResource(id = R.string.label_name),
                enabled = enabled,
                onValueChange = {
                    val addition = it.length < name.length;
                    scope.launch { animateTo((if (addition) -1 else 1) * Math.max(30f - it.length, 7f)) }
                    onChanged(it)
                }
            )
        }
    }
}