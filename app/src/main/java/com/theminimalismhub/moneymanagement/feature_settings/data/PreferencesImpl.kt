package com.theminimalismhub.moneymanagement.feature_settings.data

import android.content.Context
import com.theminimalismhub.moneymanagement.core.enviroment.LocalStorage
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences

class PreferencesImpl constructor(
    private val context: Context
) : Preferences {

    companion object {
        private lateinit var INSTANCE: Preferences
        fun getInstance(context: Context): Preferences {
            if(!::INSTANCE.isInitialized) INSTANCE = PreferencesImpl(context)
            return INSTANCE
        }
    }

    private val PREFERENCES_NAME: String = "mm_prefs"

    private object KEYS {
        const val SIMPLE_LIMIT_KEY: String = "simple_limit_key"
        const val CURRENCY_KEY: String = "currency_key"
        const val SHOW_LINE_GRAPH: String = "show_graph"
        const val COLLAPSABLE_CATEGORIES: String = "collapsable_categories"
        const val FILTER_INCOME_BY_ACCOUNT: String = "filter_income_by_account"
        const val FILTER_OUTCOME_BY_ACCOUNT: String = "filter_outcome_by_account"
    }

    override fun getSimpleLimit(): Float {
        return LocalStorage.getFloat(context, PREFERENCES_NAME, KEYS.SIMPLE_LIMIT_KEY, 1000f)
    }
    override fun setSimpleLimit(value: Float) {
        LocalStorage.putFloat(context, PREFERENCES_NAME, KEYS.SIMPLE_LIMIT_KEY, value)
    }
    override fun getCurrency(): String {
        return LocalStorage.getString(context, PREFERENCES_NAME, KEYS.CURRENCY_KEY)
    }
    override fun setCurrency(value: String) {
        LocalStorage.putString(context, PREFERENCES_NAME, KEYS.CURRENCY_KEY, value)
    }

    override fun getShowLineGraph(): Boolean {
        return LocalStorage.getBoolean(context, PREFERENCES_NAME, KEYS.SHOW_LINE_GRAPH, true)
    }

    override fun setShowLineGraph(value: Boolean) {
        LocalStorage.putBoolean(context, PREFERENCES_NAME, KEYS.SHOW_LINE_GRAPH, value)
    }

    override fun getCollapseCategories(): Boolean {
        return LocalStorage.getBoolean(context, PREFERENCES_NAME, KEYS.COLLAPSABLE_CATEGORIES, false)
    }

    override fun setCollapseCategories(value: Boolean) {
        LocalStorage.putBoolean(context, PREFERENCES_NAME, KEYS.COLLAPSABLE_CATEGORIES, value)
    }

    override fun getFilterIncomeByAccount(): Boolean {
        return LocalStorage.getBoolean(context, PREFERENCES_NAME, KEYS.FILTER_INCOME_BY_ACCOUNT, true)
    }

    override fun setFilterIncomeByAccount(value: Boolean) {
        LocalStorage.putBoolean(context, PREFERENCES_NAME, KEYS.FILTER_INCOME_BY_ACCOUNT, value)
    }

    override fun getFilterOutcomeByAccount(): Boolean {
        return LocalStorage.getBoolean(context, PREFERENCES_NAME, KEYS.FILTER_OUTCOME_BY_ACCOUNT, false)
    }

    override fun setFilterOutcomeByAccount(value: Boolean) {
        LocalStorage.putBoolean(context, PREFERENCES_NAME, KEYS.FILTER_OUTCOME_BY_ACCOUNT, value)
    }
}