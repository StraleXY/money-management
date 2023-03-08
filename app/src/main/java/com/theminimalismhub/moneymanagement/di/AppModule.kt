package com.theminimalismhub.moneymanagement.di

import android.app.Application
import com.theminimalismhub.moneymanagement.feature_categories.data.repository.CategoryRepoImpl
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.AddCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.DeleteCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.ManageCategoriesUseCases
import com.theminimalismhub.moneymanagement.feature_finances.data.repository.FinanceRepoImpl
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddFinance
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.HomeUseCases
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
    fun providesManageCategoriesUseCases(repo: CategoryRepo): ManageCategoriesUseCases {
        return ManageCategoriesUseCases(
            get = GetCategories(repo),
            add = AddCategory(repo),
            delete = DeleteCategory(repo)
        )
    }

    @Provides @Singleton
    fun providesHomeUseCases(repo: CategoryRepo): HomeUseCases {
        return HomeUseCases(
            getCategories = GetCategories(repo)
        )
    }

    @Provides @Singleton
    fun providesAddEditFinanceUseCases(repo: FinanceRepo): AddEditFinanceUseCases {
        return AddEditFinanceUseCases(
            add = AddFinance(repo)
        )
    }

}