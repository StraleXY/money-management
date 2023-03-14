package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText

@Composable
fun AddEditAccountCard(
    isOpen: Boolean,
    account: Account?,
    form: FormState<TextFieldState>
) {
    val focusManager = LocalFocusManager.current
    val name: TextFieldState = form.getState("name")
    val balance: TextFieldState = form.getState("balance")

    FloatingCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        visible = isOpen,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AccountCardLarge(
                    account = account!!,
                    overlayStrength = 0.15f,
                    scale = 1.1f
                )
            }
        }
    ) {
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
            message = name.errorMessage,
            hasError = name.hasError
        )
        OutlinedTextField(
            value = balance.value,
            onValueChange = { balance.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Name") },
            isError = balance.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) })
        )
        if(!name.hasError) Spacer(modifier = Modifier.height(4.dp))
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = name.errorMessage,
            hasError = name.hasError
        )
        Spacer(modifier = Modifier.height(24.dp))
        CRUDButtons(
            onSave = { /*TODO*/ },
            onDelete = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}