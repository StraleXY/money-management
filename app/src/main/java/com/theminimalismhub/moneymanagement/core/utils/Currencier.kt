package com.theminimalismhub.moneymanagement.core.utils

import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.round

class Currencier {
    companion object {
        fun formatAmount(value: Int): String {
            return value.toString()
        }
        fun formatAmount(value: Double): String {
            return formatAmount(value.toFloat())
        }
        fun formatAmount(value: Float): String {
            if (value % 1 == 0.0f) return value.toInt().toString()
            val df = DecimalFormat("#.##")
            return df.format(value)
        }
        fun isDecimal(value: Double): Boolean {
            return isDecimal(value.toFloat())
        }
        fun isDecimal(value: Float): Boolean {
            if (value % 1 == 0.0f) return false
            return true
        }
    }
}