package com.theminimalismhub.moneymanagement.di

import android.app.Application
import com.theminimalismhub.moneymanagement.feature_categories.data.repository.CategoryRepoImpl
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.AddCategory
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.ManageCategoriesUseCases
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

    @Provides
    @Singleton
    fun provideCategoryRepo(db: MoneyDatabase): CategoryRepo {
        return CategoryRepoImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun providesManageCategoriesUseCases(repo: CategoryRepo): ManageCategoriesUseCases {
        return ManageCategoriesUseCases(
            get = GetCategories(repo),
            add = AddCategory(repo)
        )
    }

}