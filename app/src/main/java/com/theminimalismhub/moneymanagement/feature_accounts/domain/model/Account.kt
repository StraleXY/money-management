package com.theminimalismhub.moneymanagement.feature_accounts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Account(
    val name: String,
    val balance: Double,
    val active: Boolean,
    @PrimaryKey val categoryId: Int? = null
) : Serializable
