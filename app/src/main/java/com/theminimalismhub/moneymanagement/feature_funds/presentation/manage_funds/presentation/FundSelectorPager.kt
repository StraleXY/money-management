package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils
import com.google.common.math.Quantiles.scale
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard
import kotlin.math.absoluteValue

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