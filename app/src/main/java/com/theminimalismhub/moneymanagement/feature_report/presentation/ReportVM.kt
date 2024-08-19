package com.theminimalismhub.moneymanagement.feature_report.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryBarState
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeState
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportVM @Inject constructor(
    private val useCases: HomeUseCases,
    private val preferences: Preferences

) : ViewModel() {

    private val _state = mutableStateOf(ReportState())
    val state: State<ReportState> = _state

    init {
        _state.value = _state.value.copy(
            limit = preferences.getSimpleLimit().toDouble(),
            currency = preferences.getCurrency(),
            year = LocalDateTime.now().year
        )
        setRange(_state.value.year)
        getFinances()
    }

    private fun setRange(year: Int) {
        val start: Calendar = Calendar.getInstance()
        start.set(year, 0, 1, 0, 0)
        val end: Calendar = Calendar.getInstance()
        if (year != LocalDateTime.now().year) {
            end.set(year, 11, 31, 23, 59)
        }
        Log.i("DATE_RANGE", "${start.timeInMillis} - ${end.timeInMillis}")
        _state.value = _state.value.copy(dateRange = Pair(start.timeInMillis, end.timeInMillis))
    }

    private var getFinancesJob: Job? = null
    private fun getFinances() {
        getFinancesJob?.cancel()
        getFinancesJob = useCases.getFinances(_state.value.dateRange, categoryId = null, accountId = null, mutableListOf(_state.value.selectedType), mutableListOf(true, false))
            .onEach { finance ->
                _state.value = _state.value.copy(
                    results = finance.sortedBy { it.finance.amount }.reversed(),
                    total = finance.sumOf { it.finance.amount }
                )
                getCategoryTotals()
            }
            .launchIn(viewModelScope)
    }
    private fun getCategoryTotals() {
        val bars: MutableList<CategoryAmount> = mutableListOf()
        _state.value.results.groupBy { it.category!!.categoryId }.forEach {
            bars.add(
                CategoryAmount(
                    categoryId = it.key!!,
                    accountId = it.value[0].account.accountId!!,
                    name = it.value[0].category!!.name,
                    color = it.value[0].category!!.color,
                    amount = it.value.sumOf { it.finance.amount },
                    count = it.value.count()
                )
            )
        }
        _state.value = _state.value.copy(totalPerCategory = bars.sortedBy { it.amount }.reversed())
        bars.forEach {
            _state.value.categoryBarStates[it.categoryId] = mutableStateOf(CategoryBarState.NEUTRAL)
        }
    }

    fun onEvent(event: ReportEvent) {
        when(event) {
            is ReportEvent.SwitchYear -> {
                _state.value = _state.value.copy(year = _state.value.year + event.direction)
                setRange(_state.value.year)
                getFinances()
            }
            is ReportEvent.ChangeFinanceType -> {
                _state.value = _state.value.copy(selectedType = event.type)
                getFinances()
            }
        }
    }

}