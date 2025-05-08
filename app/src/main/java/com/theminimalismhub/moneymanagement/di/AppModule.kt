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

    @Provides @Singleton fun providesContext(app: Application) : Context = app.applicationContext
    @Provides @Singleton fun providesPreferences(context: Context) : Preferences = PreferencesImpl.getInstance(context)
    @Provides @Singleton fun providesMoneyDatabase(app: Application): MoneyDatabase = MoneyDatabase.getDatabase(app)

    @Provides @Singleton fun provideCategoryRepo(db: MoneyDatabase): CategoryRepo =  CategoryRepoImpl(db.categoryDao)
    @Provides @Singleton fun provideFinanceRepo(db: MoneyDatabase): FinanceRepo =  FinanceRepoImpl(db.financeDao)
    @Provides @Singleton fun providesAccountRepo(db: MoneyDatabase): AccountRepo =  AccountRepoImpl(db.accountDao)
    @Provides @Singleton fun providesBillRepo(db: MoneyDatabase) : BillRepo =  BillRepoImpl(db.billDao)


}