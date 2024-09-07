package com.theminimalismhub.moneymanagement.di

import android.app.Application
import android.content.Context
import com.theminimalismhub.moneymanagement.feature_accounts.data.repository.AccountRepoImpl
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.*
import com.theminimalismhub.moneymanagement.feature_bills.data.repository.BillRepoImpl
import com.theminimalismhub.moneymanagement.feature_bills.domain.repository.BillRepo
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddBill
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.AddEditBillUseCases
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.DeleteBill
import com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases.GetBills
import com.theminimalismhub.moneymanagement.feature_categories.data.repository.CategoryRepoImpl
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.AddCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.DeleteCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.ManageCategoriesUseCases
import com.theminimalismhub.moneymanagement.feature_finances.data.repository.FinanceRepoImpl
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.*
import com.theminimalismhub.moneymanagement.feature_settings.data.PreferencesImpl
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun providesContext(app: Application) : Context {
        return app.applicationContext
    }

    @Provides @Singleton
    fun providesPreferences(context: Context) : Preferences {
        return PreferencesImpl.getInstance(context)
    }

    @Provides @Singleton
    fun providesMoneyDatabase(app: Application): MoneyDatabase {
        return MoneyDatabase.getDatabase(app)
    }

    @Provides @Singleton
    fun provideCategoryRepo(db: MoneyDatabase): CategoryRepo {
        return CategoryRepoImpl(db.categoryDao)
    }

    @Provides @Singleton
    fun provideFinanceRepo(db: MoneyDatabase): FinanceRepo {
        return FinanceRepoImpl(db.financeDao)
    }

    @Provides @Singleton
    fun providesAccountRepo(db: MoneyDatabase): AccountRepo {
        return AccountRepoImpl(db.accountDao)
    }

    @Provides @Singleton
    fun providesBillRepo(db: MoneyDatabase) : BillRepo {
        return BillRepoImpl(db.billDao)
    }

    @Provides @Singleton
    fun providesManageCategoriesUseCases(repo: CategoryRepo): ManageCategoriesUseCases {
        return ManageCategoriesUseCases(
            get = GetCategories(repo),
            add = AddCategory(repo),
            delete = DeleteCategory(repo)
        )
    }

    @Provides @Singleton
    fun providesAddEditFinanceUseCases(financeRepo: FinanceRepo, categoryRepo: CategoryRepo, accountRepo: AccountRepo): AddEditFinanceUseCases {
        return AddEditFinanceUseCases(
            getCategories = GetCategories(categoryRepo),
            add = AddFinance(financeRepo),
            delete = DeleteFinance(financeRepo),
            updateAccountBalance = UpdateAccountBalance(accountRepo)
        )
    }

    @Provides @Singleton
    fun providesHomeUseCases(financeRepo: FinanceRepo, categoryRepo: CategoryRepo, accountRepo: AccountRepo): HomeUseCases {
        return HomeUseCases(
            getFinances = GetFinances(financeRepo),
            getTotalPerDay = GetTotalPerDay(financeRepo, categoryRepo),
            getAccounts = GetAccounts(accountRepo)
        )
    }

    @Provides @Singleton
    fun providesManageAccountsUseCases(repo: AccountRepo, financeRepo: FinanceRepo): ManageAccountsUseCases {
        return ManageAccountsUseCases(
            add = AddAccount(repo),
            getAll = GetAccounts(repo),
            setPrimary = SetPrimaryUseCase(repo),
            delete = DeleteAccount(repo),
            addTransaction = AddTransaction(financeRepo, repo),
            getTransactions = GetFinances(financeRepo)
        )
    }

    @Provides @Singleton
    fun providesAddEditBillUseCases(categoryRepo: CategoryRepo, accountRepo: AccountRepo, billRepo: BillRepo, financeRepo: FinanceRepo) : AddEditBillUseCases {
        return AddEditBillUseCases(
            getCategories = GetCategories(categoryRepo),
            getAccounts = GetAccounts(accountRepo),
            add = AddBill(billRepo),
            get = GetBills(billRepo),
            delete = DeleteBill(billRepo),
            pay = AddFinance(financeRepo),
            updateAccountBalance = UpdateAccountBalance(accountRepo)
        )
    }

}