package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddEditBillUseCases
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.HashMap

class AddEditBillService(
    private val scope: CoroutineScope,
    private val useCases: AddEditBillUseCases,
    private val preferences: Preferences,
    private val requestClose: () -> Unit
) {

    private val _state = mutableStateOf(AddEditBillState())
    val state: State<AddEditBillState> = _state

    val formState = FormState(
        fields = listOf(
            TextFieldState(
                name = "time",
                validators = listOf(Validators.Required())
            ),
            TextFieldState(
                name = "name",
                validators = listOf(Validators.Required())
            ),
            TextFieldState(
                name = "amount",
                validators = listOf(Validators.Required())
            )
        )
    )

    init {
        _state.value = _state.value.copy(
            currency = preferences.getCurrency()
        )
        getCategories()
        getAccounts()
    }

    fun onEvent(event: AddEditBillEvent) {
        when(event) {
            is AddEditBillEvent.AccountSelected -> {
                _state.value.accountStates.forEach { (id, _) ->
                    _state.value.accountStates[id]?.value = id == event.accountId
                }
                _state.value = _state.value.copy(selectedAccountId = event.accountId)
            }
            is AddEditBillEvent.CategorySelected -> {
                _state.value.categoryStates.forEach { (id, _) ->
                    _state.value.categoryStates[id]?.value = id == event.categoryId
                }
                _state.value = _state.value.copy(selectedCategoryId = event.categoryId)
            }
            is AddEditBillEvent.AddBill -> {
                scope.launch {
                    val dayOfMonth = LocalDate.now().dayOfMonth
                    val time = (formState.fields[0].value).toInt()
                    val calendar = Calendar.getInstance()
                    if (time < dayOfMonth) calendar.add(Calendar.MONTH, 1)
                    calendar.set(Calendar.DAY_OF_MONTH, time)
                    useCases.add(
                        BillItem(
                            name = formState.fields[1].value,
                            amount = (formState.fields[2].value).toDouble(),
                            time = time,
                            type = RecurringType.MONTHLY,
                            due = calendar.timeInMillis,
                            billCategoryId = _state.value.selectedCategoryId!!,
                            billAccountId = _state.value.selectedAccountId!!,
                            isLastMonthPaid = time < dayOfMonth
                        )
                    )
                }
                requestClose()
            }
        }
    }

    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = useCases.getCategories()
            .onEach {
                val categories = it.filter { it.type == FinanceType.OUTCOME }
                _state.value = _state.value.copy(
                    categories = categories,
                    selectedCategoryId = categories[0].categoryId,
                    categoryStates = HashMap()
                )
                categories.forEach { category ->
                    category.categoryId?.let { id -> _state.value.categoryStates[id] = mutableStateOf(category.categoryId == _state.value.selectedCategoryId) }
                }

            }
            .launchIn(scope)
    }

    private var getAccountsJob: Job? = null
    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = useCases.getAccounts()
            .onEach { accounts ->
                _state.value = _state.value.copy(
                    accounts = accounts,
                    selectedAccountId = accounts.find { it.primary }?.accountId
                )
                accounts.forEach { account ->
                    account.accountId?.let { id -> _state.value.accountStates[id] = mutableStateOf(account.accountId == _state.value.selectedAccountId) }
                }
            }
            .launchIn(scope)
    }

}