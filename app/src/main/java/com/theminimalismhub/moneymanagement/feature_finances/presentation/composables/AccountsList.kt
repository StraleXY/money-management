package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.android.material.math.MathUtils
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardMini
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountChip
import kotlin.math.absoluteValue

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    states: HashMap<Int, MutableState<Boolean>>,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    currency: String = "RSD",
    spacing: Dp = 12.dp,
    selectionChanged: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.Start,
        state = listState
    ) {
        items(accounts.filter { it.active }) { account ->
            AccountCardMini(
                account = account,
                selected = states[account.accountId]!!.value,
                currency = currency
            ) { selectionChanged(account.accountId!!) }
            Spacer(modifier = Modifier.width(spacing))
        }
    }
}

@Composable
fun AccountsChips(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    states: HashMap<Int, MutableState<Boolean>>,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    spacing: Dp = 8.dp,
    selectionChanged: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.Start,
        state = listState
    ) {
        items(accounts.filter { it.active }) { account ->
            AccountChip(
                account = account,
                selected = states[account.accountId]!!.value
            ) { selectionChanged(account.accountId!!) }
            Spacer(modifier = Modifier.width(spacing))
        }
    }
}