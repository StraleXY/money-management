package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.GraphEntry
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

class GetTotalPerCategory constructor(
    private val repo: FinanceRepo,
    private val categoryRepo: CategoryRepo
) {
    operator fun invoke(range: Pair<Long, Long>, type: FinanceType, accountId: Int?, tracked: List<Boolean>) : Flow<List<CategoryAmount>> {
        return repo.getPerCategory(range, type, accountId, tracked)
    }

    suspend fun getPerDay(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?, accountId: Int?, tracked: List<Boolean>): List<GraphEntry> {
        val entries: MutableList<GraphEntry> = mutableListOf()

        val start = Calendar.getInstance()
        start.time = Date(range.first)
        Dater.setTimeToBeginningOfDay(start)
        val end = Calendar.getInstance()
        end.time = Date(range.first)
        Dater.setTimeToEndOfDay(end)

        //TODO Adjust for light theme
        val category = categoryId?.let { categoryRepo.getById(categoryId) }
        val color = category?.color ?: Color.White.toArgb()
        while (start.timeInMillis < range.second) {
            val value = repo.getAmountForTimePeriod(Pair(start.timeInMillis, end.timeInMillis), category?.type ?: type, categoryId, accountId, tracked)
            entries.add(
                GraphEntry(
                    value = value,
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