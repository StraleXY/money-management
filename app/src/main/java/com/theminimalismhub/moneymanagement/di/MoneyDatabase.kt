package com.theminimalismhub.moneymanagement.di

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),  // account: primary
        AutoMigration(2, 3),  // account: extra fields
        AutoMigration(3, 4),  // account: deleted
        AutoMigration(4, 5),  // finance item: accountTo
        AutoMigration(5, 6),  // finance item: category can be null
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

val MIGRATION_1_2 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Account ADD COLUMN primary BOOLEAN")
    }
}

class From1To2Migration : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Account ADD COLUMN primary BOOLEAN")
    }
}


fun query(block: () -> Unit) = MoneyDatabase.Instance?.queryExecutor?.execute(block)

val RoomDatabase.path: String?
    get() = openHelper.writableDatabase.path