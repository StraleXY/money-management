package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.GraphEntry
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

class GetTotalPerDay constructor(
    private val repo: FinanceRepo,
    private val categoryRepo: CategoryRepo
) {
    operator fun invoke(range: Pair<Long, Long>, type: FinanceType, items: List<Finance>, color: Int) : List<GraphEntry> {
        val entries: MutableList<GraphEntry> = mutableListOf()

        val start = Calendar.getInstance()
        start.time = Date(range.first)
        Dater.setTimeToBeginningOfDay(start)
        val end = Calendar.getInstance()
        end.time = Date(range.first)
        Dater.setTimeToEndOfDay(end)

        while (start.timeInMillis < range.second) {
            entries.add(
                GraphEntry(
                    value = items.sumOf {
                        if(it.finance.type == type && it.finance.timestamp >= start.timeInMillis && it.finance.timestamp <= end.timeInMillis) it.finance.amount
                        else 0.0
                    },
                    label = start.get(Calendar.DAY_OF_MONTH).toString(),
                    color = color
                )
            )
            start.add(Calendar.DAY_OF_MONTH, 1)
            end.add(Calendar.DAY_OF_MONTH, 1)
        }

        return entries
    }

    @Deprecated("More optimized solution in use")
    suspend fun getPerDay(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?, accountId: Int?, tracked: List<Boolean>): List<GraphEntry> {
        val entries: MutableList<GraphEntry> = mutableListOf()

        val start = Calendar.getInstance()
        start.time = Date(range.first)
        Dater.setTimeToBeginningOfDay(start)
        val end = Calendar.getInstance()
        end.time = Date(range.first)
        Dater.setTimeToEndOfDay(end)

        // Adjust for light theme
        val category = categoryId?.let { categoryRepo.getById(categoryId) }
        val color = category?.color ?: Color.White.toArgb()
        while (start.timeInMillis < range.second) {
            entries.add(
                GraphEntry(
                    value = repo.getAmountForTimePeriod(Pair(start.timeInMillis, end.timeInMillis), category?.type ?: type, categoryId, accountId, tracked),
                    label = start.get(Calendar.DAY_OF_MONTH).toString(),
                    color = color
                )
            )
            start.add(Calendar.DAY_OF_MONTH, 1)
            end.add(Calendar.DAY_OF_MONTH, 1)
        }

        return entries
    }

}