package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.android.material.math.MathUtils
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardSwipeable
import kotlin.math.absoluteValue

private enum class SwipeState {
    Collapsed,
    Expanded
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeableAccountsPager(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    currency: String,
    pagerState: PagerState,
    minAlpha: Float = 1f,
    initialCardScale: Float = 1.05f,
    selectedCardScale: Float = 1.15f,
    selectedCardStartScale: Float = 0.95f,
    balanceDelta: Double = 0.0,
    cardSpacing: Dp = 12.dp,
    onAccountSelected: (Int) -> Unit,
) {

    val swipeableState = rememberSwipeableState(initialValue = SwipeState.Collapsed)
    val density = LocalDensity.current
    val collapsedPosPx = with(density) { 132.dp.toPx() }
    val expandedPosPx = with(density) { 84.dp.toPx() }
    val anchors = mapOf(
        collapsedPosPx to SwipeState.Collapsed,
        expandedPosPx to SwipeState.Expanded
    )

    LaunchedEffect(pagerState.currentPage) {
        onAccountSelected(pagerState.currentPage)
    }
    Box(
        modifier = Modifier
            .offset { IntOffset(x = 0, y = swipeableState.offset.value.toInt()) }
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                resistance = null
            )
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 40.dp)
        ) {itemIdx ->
            if(accounts.isEmpty() || itemIdx >= accounts.size) return@HorizontalPager
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset =
                            calculateCurrentOffsetForPage(itemIdx).absoluteValue

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
                AccountCardSwipeable(
                    account = accounts[itemIdx],
                    balanceDelta = balanceDelta,
                    scale = initialCardScale,
                    currency = currency
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
