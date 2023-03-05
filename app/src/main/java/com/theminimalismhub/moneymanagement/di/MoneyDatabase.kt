package com.theminimalismhub.moneymanagement.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.data.data_source.CategoryDao
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem

@Database(
    entities = [
        Category::class,
        Account::class,
        FinanceItem::class
    ],
    version = 1
)
abstract class MoneyDatabase protected constructor() : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    companion object {
        @Volatile
        var Instance: MoneyDatabase? = null
        const val DATABASE_NAME = "tmh_money_db"
        fun getDatabase(context: Context): MoneyDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        MoneyDatabase::class.java,
                        DATABASE_NAME
                    )
//                    .createFromAsset("MONEY_InitialDB.db")
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}