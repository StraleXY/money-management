package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.CardStack
import com.theminimalismhub.moneymanagement.core.composables.DashedLine
import com.theminimalismhub.moneymanagement.core.composables.DraggableCardWithThreshold
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.HoldableActionButton
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts.AccountsPager
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.CircularTypeSelector
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ToggleTracking
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsList
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.SwipeableAccountsPager
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.BudgetFund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.CompactBudgetFund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.CompactBudgetFundNoUse
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayCompactFundCard
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddEditFinanceCard(
    state: AddEditFinanceState,
    isOpen: Boolean,
    form: FormState<TextFieldState>,
    cardToggled: (Finance?) -> Unit,
    accountSelected: (Int) -> Unit,
    typeToggled: () -> Unit,
    categorySelected: (Int) -> Unit,
    addFinance: () -> Unit,
    deleteFinance: () -> Unit,
    dateChanged: (Long) -> Unit,
    trackableToggled: () -> Unit,
    linkFund: (Fund?) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val name: TextFieldState = form.getState("name")
    val amount: TextFieldState = form.getState("amount")
    val categoryListState = rememberLazyListState()
    val accountPagerState = rememberPagerState(
        pageCount = state.accounts.filter { it.active }.size,
        initialOffscreenLimit = 2,
    )

    var budgets: List<Fund?> by remember { mutableStateOf(state.funds.filter { it.item.type == FundType.BUDGET && it.categories.map { it.categoryId }.contains(state.selectedCategoryId) }) }
    var lastOffset: Float by remember { mutableStateOf(0f) }

    fun shuffleBudgets() {
        if (budgets.isNotEmpty()) {
            val items = budgets.toMutableList()
            val first = items.removeAt(items.lastIndex)
            items.add(0, first)
            linkFund(items[items.lastIndex])
            budgets = items.toList()
        }
    }
    suspend fun setNewBudgetItems(items: List<Fund>) {
        val temp: MutableList<Fund?> = items.toMutableList()
        temp.add(0, null)
        if (temp.size == 1) delay(150)
        val fundIdx = temp.indexOfFirst { it?.finances?.map { it.financeId }?.contains(state.currentFinanceId) ?: false }
        if (fundIdx != -1) {
            val selectedBudget = temp.removeAt(fundIdx)
            temp.add(temp.lastIndex + 1, selectedBudget)
        }
        else if (state.currentFinanceId != null) {
            temp.removeAt(0)
            temp.add(temp.lastIndex + 1, null)
        }
        budgets = temp.toList()
    }

    LaunchedEffect(state.selectedCategoryId, state.funds, state.currentFinanceId) {
        if(state.selectedCategoryId == null) return@LaunchedEffect
        if(state.funds.isNotEmpty()) setNewBudgetItems(state.funds.filter { it.item.type == FundType.BUDGET && it.categories.map { it.categoryId }.contains(state.selectedCategoryId)})
        if(state.categories.isEmpty()) return@LaunchedEffect
        categoryListState.animateScrollToItem(state.categories.indexOf(state.categories.first { it.categoryId == state.selectedCategoryId } ))
    }
    LaunchedEffect(state.selectedAccountId) {
        if(state.selectedAccountId == null || state.accounts.filter { it.active }.isEmpty()) return@LaunchedEffect
        accountPagerState.scrollToPage(state.accounts.filter { it.active }.indexOf(state.accounts.filter { it.active }.first { it.accountId == state.selectedAccountId } ))
    }

    FloatingCard(
        visible = isOpen,
        header = {
            Column(
                modifier = Modifier
                    .height(464.dp)
                    .padding(top = 48.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)) {
                    AnimatedVisibility(
                        visible = state.funds.filter { it.item.type == FundType.BUDGET && it.categories.map { it.categoryId }.contains(state.selectedCategoryId) }.isNotEmpty(),
                        enter = fadeIn(tween(150)),
                        exit = fadeOut(tween(150))
                    ) {
                        CardStack(
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            budgets.forEach { fund ->
                                DraggableCardWithThreshold(
                                    idx = budgets.indexOf(fund),
                                    lastIdx = budgets.size - 1,
                                    lastOffset = lastOffset,
                                    swap = {
                                        lastOffset = it
                                        if(it != 0f) shuffleBudgets()
                                    }
                                ) {
                                    if (fund == null) CompactBudgetFundNoUse()
                                    else CompactBudgetFund(
                                        recurring = fund.item.recurringType?.label?.uppercase(),
                                        remaining = fund.item.amount.takeIf { it > 0.0 },
                                        amount = fund.item.amount.takeIf { it > 0.0 },
                                        name = fund.item.name.ifEmpty { null },
                                        colors = fund.categories.map { Colorer.getAdjustedDarkColor(it.color) }
                                    )
                                }
                            }
                        }
                    }
                }
//                LazyColumn(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(horizontal = 24.dp)
//                ) {
//                    items(state.funds.filter { it.item.type == FundType.BUDGET && it.categories.map { it.categoryId }.contains(state.selectedCategoryId) }) { fund ->
//                        CompactBudgetFund(
//                            recurring = fund.item.recurringType?.label?.uppercase(),
//                            remaining = fund.item.amount.takeIf { it > 0.0 },
//                            amount = fund.item.amount.takeIf { it > 0.0 },
//                            name = fund.item.name.ifEmpty { null },
//                            colors = fund.categories.map { Colorer.getAdjustedDarkColor(it.color) }
//                        )
//                    }
//                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    SwipeableAccountsPager(
                        accounts = state.accounts.filter { it.active },
                        currency = state.currency,
                        balanceDelta = 0.0, //try { amount.value.toDouble() } catch (ex: NumberFormatException) { 0.0 },
                        pagerState = accountPagerState,
                        minAlpha = 0.5f,
                        initialCardScale = 1.025f,
                        selectedCardStartScale = 0.875f,
                        selectedCardScale = 1.085f,
                        cardSpacing = 0.dp,
                        onAccountSelected = { idx -> accountSelected(state.accounts.filter { it.active }[idx].accountId!!) }
                    )
                    DashedLine(
                        modifier = Modifier
                            .offset(y = 17.dp)
                            .zIndex(100f),
                        dashLength = 8.dp,
                        gapLength = 4.dp
                    )
                }
            }

        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 34.dp),
            horizontalArrangement = Arrangement.Start,
            state = categoryListState
        ) {
            item {
                CircularTypeSelector(
                    selectedType = state.currentType,
                    backgroundColor = Color.Transparent,
                    borderStroke = BorderStroke(1.5.dp, MaterialTheme.run { colors.onSurface.copy(alpha = ContentAlpha.disabled) })
                ) { typeToggled() }
                Spacer(modifier = Modifier.width(7.dp))
            }
            items(state.categories) { category ->
                state.categoryStates[category.categoryId!!]?.let {
                    CategoryChip(
                        text = category.name,
                        color = Colorer.getAdjustedDarkColor(category.color),
                        isToggled = it.value,
                        onToggled = { categorySelected(category.categoryId) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        MDatePicker(
            initialTime = state.timestamp,
            datePicked = { dateChanged(it) }
        )
        Spacer(modifier = Modifier.height(4.dp))
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(100)
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = name.errorMessage,
            hasError = name.hasError
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
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
        Spacer(modifier = Modifier.height(12.dp))
        ToggleTracking(
            modifier = Modifier.padding(horizontal = 24.dp),
            action = stringResource(id = R.string.track_finance_action),
            actionHint = stringResource(id = R.string.track_finance_hint),
            toggled = state.currentTrackable
        ) { trackableToggled() }
        Spacer(modifier = Modifier.height(24.dp))
        CRUDButtons(
            onSave = {
                if(!form.validate()) return@CRUDButtons
                addFinance()
                cardToggled(null)
            },
            deleteEnabled =  state.currentFinanceId != null,
            onDelete = {
                deleteFinance()
                cardToggled(null)
            },
            onCancel = { cardToggled(null) }
        )
        Spacer(modifier = Modifier.height((9.5).dp))
    }
}

@Composable
fun MDatePicker(
    modifier: Modifier = Modifier,
    initialTime: Long = System.currentTimeMillis(),
    datePicked: (Long) -> Unit
) {

    var timestamp by remember { mutableStateOf(initialTime) }
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date(timestamp)

    val mDate = remember {
        mutableStateOf(
            "${mCalendar.get(Calendar.DAY_OF_MONTH)}/${mCalendar.get(Calendar.MONTH) + 1}/${mCalendar.get(
                Calendar.YEAR)}"
        )
    }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        R.style.my_dialog_theme,
        { _, mYear, mMonth, mDayOfMonth ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            mCalendar.clear()
            mCalendar.set(mYear, mMonth, mDayOfMonth)
            timestamp = mCalendar.timeInMillis
            datePicked(timestamp)
        },
        mCalendar.get(Calendar.YEAR),
        mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = mDate.value,
        onValueChange = { },
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { mDatePickerDialog.show() }
            .height(60.dp)
            .padding(horizontal = 36.dp),
        textStyle = MaterialTheme.typography.body1,
        label = { Text(text = "Date") },
        trailingIcon = {
            IconButton(onClick = { mDatePickerDialog.show() }) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = Icons.Default.CalendarMonth.name
                )
            }
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(disabledTextColor = MaterialTheme.colors.onBackground),
        shape = RoundedCornerShape(100)
    )
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    message: String,
    hasError: Boolean
) {
    if(hasError) {
        Text(
            modifier = modifier
                .padding(start = 4.dp),
            text = message,
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 13.sp,
                lineHeight = 15.sp
            ),
            color = MaterialTheme.colors.error,
        )
    }
}
