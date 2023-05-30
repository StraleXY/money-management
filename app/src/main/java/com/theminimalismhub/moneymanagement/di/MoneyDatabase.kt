package com.theminimalismhub.moneymanagement.di

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.theminimalismhub.moneymanagement.feature_accounts.data.data_source.AccountDao
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.data.data_source.CategoryDao
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.data_source.FinanceDao
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem

@Database(
    entities = [
        Category::class,
        Account::class,
        FinanceItem::class
    ],
    version = 8,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),  // account: primary
        AutoMigration(2, 3),  // account: extra fields
        AutoMigration(3, 4),  // account: deleted
        AutoMigration(4, 5),  // finance item: accountTo
        AutoMigration(5, 6),  // finance item: category can be null
        AutoMigration(6, 7, spec = From6To7Migration::class),  // finance item: renamed financeTo to financeFrom
        AutoMigration(7, 8),  // add: trackable flag to category and finance item
    ]
)
abstract class MoneyDatabase protected constructor() : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val financeDao: FinanceDao
    abstract val accountDao: AccountDao
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

@RenameColumn(tableName = "Finance", fromColumnName = "financeAccountIdTo", toColumnName = "financeAccountIdFrom")
class From6To7Migration : AutoMigrationSpec



fun query(block: () -> Unit) = MoneyDatabase.Instance?.queryExecutor?.execute(block)

val RoomDatabase.path: String?
    get() = openHelper.writableDatabase.path