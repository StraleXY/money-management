package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.South
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.HoldableActionButton
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.AccountsPager
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TransactionCard(
    isOpen: Boolean,
    currency: String,
    form: FormState<TextFieldState>,
    accountFrom: Account?,
    accounts: List<Account>,
    onTransaction: (Account) -> Unit,
    onCancel: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val amount: TextFieldState = form.getState("amount")
    val name: TextFieldState = form.getState("name")
    var accountTo by remember { mutableStateOf(if(accounts.isNotEmpty()) accounts[0] else null) }

    FloatingCard(
        visible = isOpen,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                accountFrom?.let {
                    AccountCardLarge(
                        modifier = Modifier.scale(1.075f),
                        account = accountFrom,
                        currency = currency,
                        balanceDelta = try { -amount.value.toDouble() } catch (ex: NumberFormatException) { 0.0 },
                        scale = 1.025f,
                        overlayStrength = 0.1f
                    )
                }
                Spacer(modifier = Modifier.height(34.dp))
                Icon(imageVector = Icons.Default.South, contentDescription = Icons.Default.South.name)
                AccountsPager(
                    accounts = accounts,
                    currency = currency,
                    balanceDelta = try { amount.value.toDouble() } catch (ex: NumberFormatException) { 0.0 },
                    pagerState = rememberPagerState(
                        pageCount = accounts.size,
                        initialOffscreenLimit = 2,
                    ),
                    minAlpha = 0.5f,
                    cardOverlayStrength = 0.1175f,
                    initialCardScale = 1.025f,
                    selectedCardStartScale = 0.85f,
                    selectedCardScale = 1.075f,
                    cardSpacing = 0.dp,
                    onAccountSelected = { accountTo = accounts[it] }
                )
            }
        }
    ) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Name") },
            isError = name.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
            shape = RoundedCornerShape(100)
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = amount.errorMessage,
            hasError = amount.hasError
        )
        if(!name.hasError) Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Amount") },
            isError = amount.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
            shape = RoundedCornerShape(100)
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = amount.errorMessage,
            hasError = amount.hasError
        )
        if(!amount.hasError) Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .height(47.dp)
                .padding(horizontal = 33.dp)
                .fillMaxWidth()
                .shadedBackground(Shade.LIGHT, RoundedCornerShape(100)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(
                    modifier = Modifier,
                    onClick = onCancel,
                    shape = RoundedCornerShape(100)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "",
                            modifier = Modifier
                                .size(26.dp)
                                .alpha(0.8f)
                                .rotate(45f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.action_cancel),
                            style = MaterialTheme.typography.button,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.primary, RoundedCornerShape(100))
            ) {
                HoldableActionButton(
                    modifier = Modifier
                        .padding(start = 9.dp, end = 14.dp)
                        .align(Alignment.Center),
                    text = stringResource(id = R.string.ma_confirm_transaction),
                    icon = Icons.Default.Check,
                    textStyle = MaterialTheme.typography.button,
                    duration = 2500,
                    circleColor = Color.Transparent,
                    alternatedColor = MaterialTheme.colors.onPrimary,
                    iconColor = MaterialTheme.colors.onPrimary,
                    onHold = { onTransaction(accountTo!!) }
                )
            }
        }
        Spacer(modifier = Modifier.height((9.5).dp))
    }
}