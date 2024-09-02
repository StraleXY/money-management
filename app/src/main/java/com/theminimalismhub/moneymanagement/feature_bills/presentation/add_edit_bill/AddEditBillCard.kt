package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.TextFieldState
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.CircularTypeSelector
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsList
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip


@Composable
fun AddEditBillCard(
    isOpen: Boolean,
    vm: AddEditBillService
) {

    val state = vm.state.value

    val categoryListState = rememberLazyListState()
    val accountListState = rememberLazyListState()

    LaunchedEffect(state.selectedCategoryId) {
        if(state.selectedCategoryId == null || state.categories.isEmpty()) return@LaunchedEffect
        categoryListState.animateScrollToItem(state.categories.indexOf(state.categories.first { it.categoryId == state.selectedCategoryId } ))
    }
    LaunchedEffect(state.selectedAccountId) {
        if(state.selectedAccountId == null || state.accounts.isEmpty()) return@LaunchedEffect
        accountListState.animateScrollToItem(state.accounts.indexOf(state.accounts.first { it.accountId == state.selectedAccountId } ))
    }

    val focusManager = LocalFocusManager.current
    val time: TextFieldState  = vm.formState.getState("time")
    val name: TextFieldState  = vm.formState.getState("name")
    val amount: TextFieldState  = vm.formState.getState("amount")

    FloatingCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        visible = isOpen,
        header = {
            AccountsList(
                accounts = state.accounts,
                states = state.accountStates,
                currency = state.currency,
                listState = accountListState
            ) { vm.onEvent(AddEditBillEvent.AccountSelected(it)) }
        }
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
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = time.value,
            onValueChange = { time.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Due Day") },
            isError = time.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = time.errorMessage,
            hasError = time.hasError
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Name") },
            isError = name.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
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
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Amount") },
            isError = amount.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus(true) })
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
            deleteEnabled =  state.currentFinanceId != null,
            onDelete = {
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}