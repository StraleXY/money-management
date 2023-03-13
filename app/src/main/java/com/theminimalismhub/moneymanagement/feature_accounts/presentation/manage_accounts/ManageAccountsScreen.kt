package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size,
        initialOffscreenLimit = 1,
    )

    val density = LocalDensity.current.density
    val root = LocalView.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    var accountsPagerHeight by remember { mutableStateOf(0.dp) }
    var screenHeight by remember { mutableStateOf(root.height / density) }
    val scroll: ScrollState = rememberScrollState(0)

    val TAG = "ACCOUNT"
    LaunchedEffect(screenHeight) { Log.w(TAG, "Screen Height $screenHeight | Density $density")}
    LaunchedEffect(headerHeight) { Log.w(TAG, "Header Height $headerHeight")}
    LaunchedEffect(accountsPagerHeight) { Log.w(TAG, "Cards Pager Height $accountsPagerHeight")}


    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            item {
                Column(modifier = Modifier.onSizeChanged { headerHeight = Dp(it.height / density) }) {
                    ScreenHeader(
                        title = "Management Accounts",
                        hint = "Track your balance across multiple accounts!"
                    )
                    AccountsPager(
                        modifier = Modifier.onSizeChanged { accountsPagerHeight = Dp(it.height / density) },
                        accounts = state.accounts,
                        pagerState = pagerState,
                        onAccountSelected = { vm.onEvent(ManageAccountsEvent.CardSelected(it)) }
                    )
                    AccountActions(
                        enabled = !pagerState.isScrollInProgress,
                        account = state.selectedAccount,
                        onToggleActivate = { vm.onEvent(ManageAccountsEvent.ToggleActive) }
                    )
                }
            }

        }
        var textTrans = 0f
        Column() {
            Spacer(Modifier.height(accountsPagerHeight))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .graphicsLayer {
                        translationY =
                            ((headerHeight - accountsPagerHeight - scroll.value.dp).toPx()).coerceAtLeast(
                                0f
                            )
                    }
                    .verticalScroll(scroll)
            ) {
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            val cardTranslationY = ((headerHeight - accountsPagerHeight - scroll.value.dp).toPx()).coerceAtLeast(0f)
                            textTrans = if (cardTranslationY > 0) scroll.value.dp.toPx() / 2 else textTrans
                            translationY = textTrans
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .shadow(8.dp)
                            .background(Color(0XFF212121))
                    ) {
                        repeat(50) {
                            Text(
                                text = "In the modern design world, Lorem Ipsum is the industry standard when placing dummy text onto an unfinished page, whether it's a newspaper, magazine, or advertisement. The Latin text was first used in the 16th century, when a printer scrambled a row of type (known as a \"galley\") so it could be used in a book that showcased the type's quality. This text saw a resurgence when electronic typesetting became popular in the 1960s, mainly because the French typography company Letraset started selling sheets with Lorem Ipsum.",
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                        }
                        Text(text = "LRAJK")
                        Spacer(modifier = Modifier.height(160.dp))
                    }
                }
            }
        }
    }
}

