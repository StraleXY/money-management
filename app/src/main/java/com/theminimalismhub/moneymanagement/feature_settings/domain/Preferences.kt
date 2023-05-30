package com.theminimalismhub.moneymanagement.feature_settings.domain

interface Preferences {
    fun getSimpleLimit() : Float
    fun setSimpleLimit(value: Float)
}