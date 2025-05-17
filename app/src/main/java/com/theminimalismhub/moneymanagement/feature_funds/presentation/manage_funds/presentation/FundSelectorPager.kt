package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard
import kotlin.math.absoluteValue
import kotlin.math.max

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FundSelectorPager(
    modifier: Modifier = Modifier,
    minAlpha: Float = 1f,
    initialCardScale: Float = 1.05f,
    selectedCardScale: Float = 1.15f,
    selectedCardStartScale: Float = 0.95f,
    cardSpacing: Dp = 0.dp,
) {

    val pagerState = rememberPagerState(3, 0)
    val width = with(LocalDensity.current) { LocalView.current.width.toDp() }

    val messages = listOf(
        "Budget",
        "Reservation",
        "Savings"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(start = 24.dp)
                .fillMaxWidth()
        ) {
            WipeRevealText(
                messages = messages,
                pagerState = pagerState
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 40.dp, bottom = 40.dp)
        ) { idx ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(idx).absoluteValue

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
                Box(
                    modifier = Modifier
                        .width(((width - 112.dp) * initialCardScale))
                ) {
                    Box(
                        modifier = Modifier
                            .scale(0.85f)
                    ) {
                        DisplayFundCard(
                            fund = Fund(
                                item = FundItem(
                                    name = "Unknown",
                                    type = FundType.get(idx)!!,
                                    amount = 0.0
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WipeRevealText(
    width: Dp = 150.dp,
    messages: List<String>,
    intervalMillis: Long = 1000,
    pagerState: PagerState
) {
    var currentIndex by remember { mutableStateOf(0) }
    var animationProgress by remember { mutableStateOf(1f) } // 1 = fully revealed

    val nextIndex = (pagerState.currentPage + 1) % messages.size

    // Start a looping effect
//    LaunchedEffect(currentIndex) {
//        while (true) {
//            delay(intervalMillis)
//
//            // Animate wipe (0f to 1f)
//            val duration = 3000
//            animate(
//                initialValue = 0f,
//                targetValue = 1f,
//                animationSpec = tween(durationMillis = duration)
//            ) { value, _ ->
//                animationProgress = value
//            }
//
//            // Once done, switch text
//            currentIndex = nextIndex
//            animationProgress = 0f
//        }
//    }

    


    Box(
        modifier = Modifier
            .height(32.dp)
            .clipToBounds()
    ) {
        // Bottom text (next one)
        Text(
            text = messages[nextIndex],
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .width(width)
                .align(Alignment.Center)
                .padding(
                    end = 0.dp,
                    start = max(24 * (2 - pagerState.currentPageOffset), 0f).dp
                )
                .graphicsLayer {
                    clip = true
                    shape = RectangleShape
                }
                .drawWithContent {
                    val wipeWidth = size.width * (1 - pagerState.currentPageOffset)
                    clipRect(
                        left = wipeWidth,
                        top = 0f,
                        right = size.width,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            color = MaterialTheme.colors.onBackground
        )
        // Top text with wipe animation (current one being revealed away)
        Text(
            text = messages[pagerState.currentPage],
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .width(width)
                .align(Alignment.Center)
                .padding(
                    end = max(24 * pagerState.currentPageOffset, 0f).dp,
                    start = max(24 * (1 - pagerState.currentPageOffset), 0f).dp
                )
                .graphicsLayer {
                    clip = true
                    shape = RectangleShape
                }
                .drawWithContent {
                    val wipeWidth = size.width * (1 - pagerState.currentPageOffset)
                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = wipeWidth,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            color = MaterialTheme.colors.onBackground
        )
    }
}
