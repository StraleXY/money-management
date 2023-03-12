package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils.lerp
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddNewAccount
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size + 1,
        initialOffscreenLimit = 2,
    )

    LaunchedEffect(pagerState.currentPage) {
        Log.d("PAGER", pagerState.currentPage.toString())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ScreenHeader(
                    title = "Management Accounts",
                    hint = "Track your balance across multiple accounts!"
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 40.dp)
                ) {itemIdx ->
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset =
                                    calculateCurrentOffsetForPage(itemIdx).absoluteValue

                                lerp(
                                    0.95f, 1.15f, 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                            }
                            .padding(horizontal = 12.dp)
                    ) {
                        if(state.accounts.elementAtOrNull(itemIdx) == null) AddNewAccount(scale = 1.05f) { }
                        else AccountCardLarge(
                            account = state.accounts[itemIdx],
                            scale = 1.05f
                        )
                    }
                }
//                LazyRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(horizontal = 24.dp),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    items(state.accounts) { account ->
//
//                        Spacer(modifier = Modifier.width(8.dp))
//                    }
//                    item {
//                        AddNewAccount {
//
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}