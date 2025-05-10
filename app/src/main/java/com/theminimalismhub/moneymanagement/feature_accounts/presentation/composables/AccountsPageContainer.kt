package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.PanelState
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.toOffset
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountsPageContainer(
    header: @Composable ColumnScope.() -> Unit,
    accountsPager: @Composable ColumnScope.() -> Unit,
    accountButtons: @Composable ColumnScope.() -> Unit,
    accountStats: @Composable LazyItemScope.() -> Unit,
    items: @Composable LazyItemScope.() -> Unit
) {

    val density = LocalDensity.current
    val pageHeight = with(LocalDensity.current) { LocalView.current.height.toDp() }
    val headerHeight = 157.dp
    val pagerHeight = 277.dp
    val accountButtonsHeight = 130.dp
    fun topSectionHeight() : Dp = headerHeight + pagerHeight + accountButtonsHeight
    val height = pageHeight - pagerHeight
    val swipeState = rememberSwipeableState(initialValue = PanelState.Expanded)
    val anchors = with(LocalDensity.current) { mapOf(topSectionHeight().toPx() to PanelState.Expanded, pagerHeight.toPx() to PanelState.Collapsed) }
    fun calcHeaderOffset(offset: Int): Int {
        var final: Int = 0
        with(density) {
            val fraction = (offset - topSectionHeight().toPx()) / (pagerHeight.toPx() - topSectionHeight().toPx())
            final = (headerHeight * fraction).toPx().roundToInt()
        }
        return final
    }
    val listState: LazyListState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val currentOffset = swipeState.offset.value
                return if (delta < 0 && currentOffset > anchors.keys.min()) {
                    // User scrolls up, collapse the sheet
                    swipeState.performDrag(delta).toOffset()
                } else Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                return if (delta > 0 && listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                    // User scrolls down and list is at top, expand the sheet
                    swipeState.performDrag(delta).toOffset()
                } else Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                swipeState.performFling(available.y)
                return if(swipeState.currentValue == PanelState.Collapsed) Velocity(0f, available.y / 3 * 2) else available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                swipeState.performFling(available.y)
                return Velocity.Zero
            }
        }
    }


    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = -calcHeaderOffset(swipeState.offset.value.roundToInt())) }
        ) {
            header()
            accountsPager()
            accountButtons()
        }
        Box(
            Modifier
                .offset { IntOffset(x = 0, y = swipeState.offset.value.roundToInt()) }
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colors.background)
                .nestedScroll(nestedScrollConnection)
                .swipeable(
                    state = swipeState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
        ) {
            LazyColumn(
                modifier = Modifier.shadedBackground(Shade.DARK),
                state = listState
            ) {
                item {
                    accountStats()
                    items()
                }
            }
        }
    }
}


@Composable
private fun Header() {
    PlaceholderItem(100.dp, Color.Gray,"Header")
}

@Composable
private fun AccountsPager() {
    PlaceholderItem(240.dp, Color.White, "Accounts")
}

@Composable
private fun AccountButtons() {
    PlaceholderItem(140.dp, Color.Gray, "Account Buttons")
}

@Composable
private fun AccountStats() {
    PlaceholderItem(140.dp, Color.DarkGray, "Account Stats")
}

@Composable
private fun Transaction() {
    PlaceholderItem(50.dp, Color.Black, "Transaction")
}

@Composable
private fun PlaceholderItem(
    height: Dp,
    color: Color,
    text: String
) {
    Column(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(color),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = text,
            style = MaterialTheme.typography.h4,
            color = if(color == Color.White) Color.Black else Color.White
        )
    }
}