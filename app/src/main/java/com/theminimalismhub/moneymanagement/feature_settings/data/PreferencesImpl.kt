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
}