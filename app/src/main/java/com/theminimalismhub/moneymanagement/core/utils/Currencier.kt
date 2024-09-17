package com.theminimalismhub.moneymanagement.core.utils

import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.round

class Currencier {
    companion object {
        fun formatAmount(value: Int): String {
            return value.toString()
        }
        fun formatAmount(value: Double, isCompact: Boolean = false): String {
            return formatAmount(value.toFloat(), isCompact)
        }
        fun formatAmount(value: Float, isCompact: Boolean = false): String {
            if (value % 1 == 0.0f) {
                return if (value > 99999 && isCompact) "${(value / 1000).toInt()}k"
                else value.toInt().toString()
            }
            // TODO Fix displaying of large decimal numbers
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