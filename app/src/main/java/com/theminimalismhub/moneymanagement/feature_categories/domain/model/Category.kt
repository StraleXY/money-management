package com.theminimalismhub.moneymanagement.feature_categories.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.domain.model.FinanceType
import java.io.Serializable

@Entity
data class Category(
    val name: String,
    val color: Int,
    val type: FinanceType,
    val isDeleted: Boolean,
    @PrimaryKey val categoryId: Int? = null
) : Serializable
