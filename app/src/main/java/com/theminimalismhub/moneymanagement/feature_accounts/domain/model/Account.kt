package com.theminimalismhub.moneymanagement.feature_accounts.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import java.io.Serializable

@Entity
data class Account(
    val name: String,
    val balance: Double,
    val active: Boolean,
    @PrimaryKey val accountId: Int? = null,
    @ColumnInfo(name = "deleted", defaultValue = "0") val deleted: Boolean,
    @ColumnInfo(name = "primary", defaultValue = "0") val primary: Boolean,
    @ColumnInfo(name = "type", defaultValue = "CASH") val type: AccountType,
    @ColumnInfo(name = "description", defaultValue = "") val description: String = ""
) : Serializable


