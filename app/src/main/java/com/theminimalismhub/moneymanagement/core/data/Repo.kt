package com.theminimalismhub.moneymanagement.core.data

import kotlinx.coroutines.flow.Flow

interface Repo<T> : RepoBase<T> {
    suspend fun delete(item: T)
}

interface RepoBase<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getById(id: Int): T?
    suspend fun insert(item: T): Long
    suspend fun delete(id: Int)
}