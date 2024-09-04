package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddEditBillUseCases
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.HashMap
import java.util.TimeZone

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
            ),
            TextFieldState(
                name = "interval",
                validators = if(_state.value.recurringType == RecurringType.INTERVAL) listOf(Validators.Required()) else emptyList()
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

    @SuppressLint("SimpleDateFormat")
    fun initBill(bill: Bill) {
        _state.value = _state.value.copy(
            currentBillId = bill.bill.billId,
            recurringType = bill.bill.type
        )
        bill.category?.categoryId?.let { onEvent(AddEditBillEvent.CategorySelected(it)) }
        bill.account.accountId?.let { onEvent(AddEditBillEvent.AccountSelected(it)) }
        when(bill.bill.type) {
            RecurringType.MONTHLY -> formState.fields[0].change(bill.bill.time.toString())
            RecurringType.INTERVAL -> {

                formState.fields[0].change(SimpleDateFormat("dd").format(bill.bill.due).toInt().toString())
                formState.fields[3].change(bill.bill.time.toString())
            }
        }
        formState.fields[1].change(bill.bill.name)
        formState.fields[2].change(bill.bill.amount.toInt().toString())
    }

    fun clear() {
        _state.value = _state.value.copy(
            currentBillId = null,
            recurringType = RecurringType.MONTHLY
        )
        onEvent(AddEditBillEvent.CategorySelected(null))
        onEvent(AddEditBillEvent.AccountSelected(null))
        formState.fields[0].change("")
        formState.fields[1].change("")
        formState.fields[2].change("")
        formState.fields[3].change("")
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
            is AddEditBillEvent.SelectRecurring -> {
                _state.value = _state.value.copy(recurringType = event.type)
            }
            is AddEditBillEvent.AddBill -> {
                scope.launch {
                    val dayOfMonth = LocalDate.now().dayOfMonth
                    var dueDay = (formState.fields[0].value).toInt()
                    val interval = (formState.fields[3].value.ifEmpty { "0" }).toInt()
                    val calendar = Calendar.getInstance()
                    if (dueDay < dayOfMonth) {
                        when(_state.value.recurringType) {
                            RecurringType.MONTHLY -> {
                                calendar.add(Calendar.MONTH, 1)
                                calendar.set(Calendar.DAY_OF_MONTH, dueDay)
                            }
                            RecurringType.INTERVAL -> {
                                calendar.set(Calendar.DAY_OF_MONTH, dueDay)
                                calendar.add(Calendar.DAY_OF_MONTH, interval)
                                dueDay = calendar.get(Calendar.DAY_OF_MONTH)
                            }
                        }
                    }
                    useCases.add(
                        BillItem(
                            name = formState.fields[1].value,
                            amount = (formState.fields[2].value).toDouble(),
                            time = if(_state.value.recurringType == RecurringType.MONTHLY) dueDay else interval,
                            type = _state.value.recurringType,
                            due = calendar.timeInMillis,
                            billCategoryId = _state.value.selectedCategoryId!!,
                            billAccountId = _state.value.selectedAccountId!!,
                            isLastMonthPaid = dueDay < dayOfMonth,
                            billId = _state.value.currentBillId
                        )
                    )
                    requestClose()
                }
            }
            is AddEditBillEvent.DeleteBill -> {
                _state.value.currentBillId?.let {
                    scope.launch {
                        useCases.delete(it)
                        requestClose()
                    }
                }
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