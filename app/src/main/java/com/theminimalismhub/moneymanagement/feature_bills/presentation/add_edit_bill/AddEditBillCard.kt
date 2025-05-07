package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.TextFieldState
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import com.theminimalismhub.moneymanagement.feature_bills.presentation.ManageBillsEvent
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsChips
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsList
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.ToggleChip


@Composable
fun AddEditBillCard(
    isOpen: Boolean,
    vm: AddEditBillService,
    onCancel: () -> Unit
) {

    val state = vm.state.value

    val categoryListState = rememberLazyListState()
    val accountListState = rememberLazyListState()

    LaunchedEffect(state.selectedCategoryId) {
        Log.i("CATEGORY", "Changed to id: ${state.selectedCategoryId} | Is empty? ${state.categories.isEmpty()}")
        if(state.selectedCategoryId == null || state.categories.isEmpty()) return@LaunchedEffect
        categoryListState.animateScrollToItem(state.categories.indexOf(state.categories.first { it.categoryId == state.selectedCategoryId } ))
    }
    LaunchedEffect(state.selectedAccountId) {
        if(state.selectedAccountId == null || state.accounts.isEmpty()) return@LaunchedEffect
        accountListState.animateScrollToItem(state.accounts.indexOf(state.accounts.first { it.accountId == state.selectedAccountId } ))
    }

    val focusManager = LocalFocusManager.current
    val time: TextFieldState  = vm.formState.getState("time")
    val interval: TextFieldState  = vm.formState.getState("interval")
    val name: TextFieldState  = vm.formState.getState("name")
    val amount: TextFieldState  = vm.formState.getState("amount")

    FloatingCard(
        visible = isOpen,
        header = {}
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 34.dp),
            horizontalArrangement = Arrangement.Start,
            state = categoryListState
        ) {
            items(state.categories) { category ->
                state.categoryStates[category.categoryId!!]?.let {
                    CategoryChip(
                        text = category.name,
                        color = Colorer.getAdjustedDarkColor(category.color),
                        isToggled = it.value,
                        onToggled = { vm.onEvent(AddEditBillEvent.CategorySelected(category.categoryId)) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        AccountsChips(
            accounts = state.accounts,
            states = state.accountStates,
            listState = accountListState
        ) { vm.onEvent(AddEditBillEvent.AccountSelected(it)) }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 34.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            item {
                SelectableChip(
                    label = "Payed Monthly",
                    icon = Icons.Default.Today,
                    onClick = { vm.onEvent(AddEditBillEvent.SelectRecurring(RecurringType.MONTHLY)) },
                    selected = state.recurringType == RecurringType.MONTHLY
                )
                Spacer(modifier = Modifier.width(8.dp))
                SelectableChip(
                    label = "Payed in Interval",
                    icon = Icons.Default.Timelapse,
                    onClick = { vm.onEvent(AddEditBillEvent.SelectRecurring(RecurringType.INTERVAL)) },
                    selected = state.recurringType == RecurringType.INTERVAL
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = time.value,
            onValueChange = { time.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Due Day") },
            isError = time.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(100)
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = time.errorMessage,
            hasError = time.hasError
        )
        Spacer(modifier = Modifier.height(4.dp))
        AnimatedVisibility(visible = state.recurringType == RecurringType.INTERVAL) {
            OutlinedTextField(
                value = interval.value,
                onValueChange = { interval.change(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 36.dp),
                textStyle = MaterialTheme.typography.body1,
                label = { Text(text = "Interval [Days]") },
                isError = interval.hasError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = RoundedCornerShape(100)
            )
        }
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = interval.errorMessage,
            hasError = interval.hasError
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
            message = amount.errorMessage,
            hasError = amount.hasError
        )
        Spacer(modifier = Modifier.height(4.dp))
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
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus(true) }),
            shape = RoundedCornerShape(100)
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = amount.errorMessage,
            hasError = amount.hasError
        )
        Spacer(modifier = Modifier.height(24.dp))
        CRUDButtons(
            onSave = {
                if(!vm.formState.validate()) return@CRUDButtons
                vm.onEvent(AddEditBillEvent.AddBill)
            },
            deleteEnabled =  state.currentBillId != null,
            onDelete = { vm.onEvent(AddEditBillEvent.DeleteBill) },
            onCancel = onCancel
        )
        Spacer(modifier = Modifier.height((9.5).dp))
    }
}