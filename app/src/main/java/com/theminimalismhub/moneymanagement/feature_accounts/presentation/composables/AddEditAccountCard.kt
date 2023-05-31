package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance.ErrorText

@Composable
fun AddEditAccountCard(
    isOpen: Boolean,
    type: AccountType,
    form: FormState<TextFieldState>,
    currency: String,
    accountTypeStates: HashMap<AccountType, MutableState<Boolean>>,
    onTypeChanged: (AccountType) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val name: TextFieldState = form.getState("name")
    val balance: TextFieldState = form.getState("balance")
    val description: TextFieldState = form.getState("description")
    val descriptionVisible by remember { mutableStateOf(type == AccountType.CARD) }

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
                NewAccountExampleCard(
                    modifier = Modifier.scale(1.15f),
                    name = name.value,
                    balance = balance.value,
                    description = description.value,
                    currency = currency,
                    type = type,
                    overlayStrength = 0.1f,
                    scale = 1.05f
                )
            }
        }
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 36.dp)
        ) {
            items(accountTypeStates.toSortedMap().keys.toList()) { type ->
                AccountTypeCard(
                    type = type,
                    selected = accountTypeStates[type]!!.value
                ) { onTypeChanged(type) }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

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
        if(!name.hasError) Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = balance.value,
            onValueChange = { balance.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Balance") },
            isError = balance.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = if(descriptionVisible) ImeAction.Next else ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(true) },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = balance.errorMessage,
            hasError = balance.hasError
        )
        if(!balance.hasError) Spacer(modifier = Modifier.height(4.dp))

        AnimatedVisibility(
            visible = type == AccountType.CARD
        ) {
            Column {
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.change(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    textStyle = MaterialTheme.typography.body1,
                    label = { Text(text = if(type == AccountType.CARD) "Last 4 Digits" else "Description") },
                    isError = description.hasError,
                    keyboardOptions = KeyboardOptions(keyboardType = if(type == AccountType.CARD) KeyboardType.Number else KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) })
                )
                ErrorText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    message = description.errorMessage,
                    hasError = description.hasError
                )
                if(!description.hasError) Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        CRUDButtons(
            onSave = onSave,
            onDelete = onDelete
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}