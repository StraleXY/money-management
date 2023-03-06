package com.theminimalismhub.moneymanagement.feature_categories.data.repository

import com.theminimalismhub.moneymanagement.feature_categories.data.data_source.CategoryDao
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow

class CategoryRepoImpl(
    private val dao: CategoryDao
) : CategoryRepo {

    override fun getAll(): Flow<List<Category>> {
        return dao.getCategories()
    }

    override suspend fun getById(id: Int): Category? {
        return dao.getCategoryById(id)
    }

    override suspend fun insert(item: Category): Long {
        return dao.insertCategory(item)
    }

    override suspend fun delete(id: Int) {
        dao.getCategoryById(id)?.let { dao.insertCategory(it.copy(isDeleted = true)) }
    }

    override suspend fun delete(item: Category) {
        dao.insertCategory(item.copy(isDeleted = true))
    }
}