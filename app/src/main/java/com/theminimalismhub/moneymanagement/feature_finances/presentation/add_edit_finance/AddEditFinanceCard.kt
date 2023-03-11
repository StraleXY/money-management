package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.ActionChip
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.core.composables.HoldableActionButton
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.CircularTypeSelector
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsList
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.CategoryChip
import java.util.*

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
    dateChanged: (Long) -> Unit

) {
    val focusManager = LocalFocusManager.current
    val name: TextFieldState = form.getState("name")
    val amount: TextFieldState = form.getState("amount")

    FloatingCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        visible = isOpen,
        header = {
            AccountsList(
                accounts = state.accounts,
                states = state.accountStates
            ) { accountSelected(it) }
        }
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 34.dp),
            horizontalArrangement = Arrangement.Start
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
                        color = Color(category.color),
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
            value = amount.value,
            onValueChange = { amount.change(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Amount") },
            isError = amount.hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) })
        )
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            message = amount.errorMessage,
            hasError = amount.hasError
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HoldableActionButton(
                modifier = Modifier,
                text = stringResource(id = R.string.action_delete),
                icon = Icons.Default.Delete,
                textStyle = MaterialTheme.typography.button,
                duration = 2500,
                circleColor = Color.Transparent,
                alternatedColor = MaterialTheme.colors.error,
                iconColor = MaterialTheme.colors.onBackground,
                onHold = {
                    deleteFinance()
                    cardToggled(null)
                },
                enabled = state.currentFinanceId != null
            )
            Spacer(modifier = Modifier.width(12.dp))
            ActionChip(
                text = stringResource(id = R.string.action_save),
                icon = Icons.Default.Save,
                textStyle = MaterialTheme.typography.button,
                borderThickness = 0.dp,
                backgroundStrength = 0f,
                modifier = Modifier,
                onClick = {
                    if(!form.validate()) return@ActionChip
                    addFinance()
                    cardToggled(null)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
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
        colors = TextFieldDefaults.outlinedTextFieldColors(disabledTextColor = MaterialTheme.colors.onBackground)
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