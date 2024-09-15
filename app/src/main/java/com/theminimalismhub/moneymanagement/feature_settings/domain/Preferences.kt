package com.theminimalismhub.moneymanagement.feature_settings.domain

interface Preferences {
    fun getSimpleLimit() : Float
    fun setSimpleLimit(value: Float)
    fun getCurrency() : String
    fun setCurrency(value: String)
    fun getShowLineGraph() : Boolean
    fun setShowLineGraph(value: Boolean)
    fun getCollapseCategories() : Boolean
    fun setCollapseCategories(value: Boolean)
    fun getFilterIncomeByAccount() : Boolean
    fun setFilterIncomeByAccount(value: Boolean)
    fun getFilterOutcomeByAccount() : Boolean
    fun setFilterOutcomeByAccount(value: Boolean)
    fun getSwipeableNavigation() : Boolean
    fun setSwipeableNavigation(value: Boolean)
}