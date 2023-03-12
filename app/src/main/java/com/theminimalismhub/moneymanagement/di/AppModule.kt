package com.theminimalismhub.moneymanagement.di

import android.app.Application
import com.theminimalismhub.moneymanagement.feature_accounts.data.repository.AccountRepoImpl
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.AddAccount
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.ManageAccountsUseCases
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.UpdateAccountBalance
import com.theminimalismhub.moneymanagement.feature_categories.data.repository.CategoryRepoImpl
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.AddCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.DeleteCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.ManageCategoriesUseCases
import com.theminimalismhub.moneymanagement.feature_finances.data.repository.FinanceRepoImpl
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
            getTotalPerCategory = GetTotalPerCategory(financeRepo, categoryRepo),
            getAccounts = GetAccounts(accountRepo)
        )
    }

    @Provides @Singleton
    fun providesManageAccountsUseCases(repo: AccountRepo): ManageAccountsUseCases {
        return ManageAccountsUseCases(
            add = AddAccount(repo),
            getAccounts = GetAccounts(repo)
        )
    }

}