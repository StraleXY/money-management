package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.VerticalAnimatedVisibility
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsChipsSelectable
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChipsSelectable
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.RecurringTypeSelector

@Composable
fun AddEditFundCard(
    isOpen: Boolean,
    isNew: Boolean,
    fundType: FundType,
    onTypeFundSelected: (FundType) -> Unit,
    accounts: List<Account>,
    selectedAccounts: List<Account>,
    onAccountIdsSelected: (List<Int>) -> Unit,
    categories: List<Category>,
    selectedCategories: List<Category>,
    onCategoryIdsSelected: (List<Int>) -> Unit,
    selectedRecurring: RecurringType?,
    onRecurringSelected: (RecurringType) -> Unit,
    form: FormState<TextFieldState>,
    requestCardToClose: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val name: TextFieldState = form.getState("name")
    val amount: TextFieldState = form.getState("amount")

    FloatingCard(
        visible = isOpen,
        header = {
            FundSelectorPager(
                name = name.value,
                amount = amount.value.toDoubleOrNull() ?: 0.0,
                accounts = selectedAccounts,
                categories = selectedCategories,
                recurring = selectedRecurring,
                initialType = fundType,
                enabled = isNew,
                onTypeFundSelected = { onTypeFundSelected(it) }
            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        VerticalAnimatedVisibility(visible = fundType == FundType.RESERVATION) {
            AccountsChipsSelectable(
                accounts = accounts,
                selectedAccounts = selectedAccounts,
                multiple = fundType == FundType.BUDGET,
                selectionChanged = { onAccountIdsSelected(it) }
            )
        }

        VerticalAnimatedVisibility(visible = fundType != FundType.SAVINGS) {
            CategoryChipsSelectable(
                categories = categories,
                selectedCategories = selectedCategories,
                multiple = fundType == FundType.BUDGET,
                selectionChanged = { onCategoryIdsSelected(it) }
            )
        }

        VerticalAnimatedVisibility(visible = fundType == FundType.BUDGET) {
            RecurringTypeSelector(
                selectedRecurringType = selectedRecurring,
                onRecurringTypeSelected = onRecurringSelected
            )
        }

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
        CRUDButtons(
            onSave = { onSave() },
            deleteEnabled = !isNew,
            onDelete = { onDelete() },
            onCancel = { requestCardToClose() }
        )
        Spacer(modifier = Modifier.height((9.5).dp))
    }
}