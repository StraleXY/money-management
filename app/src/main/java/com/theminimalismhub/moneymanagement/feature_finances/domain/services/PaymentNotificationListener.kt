package com.theminimalismhub.moneymanagement.feature_finances.domain.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class PaymentNotificationListener : NotificationListenerService() {

    @Inject
    lateinit var useCases: AddEditFinanceUseCases

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private object Wallets {
        const val GOOGLE = "com.google.android.apps.walletnfcrel"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        val notifications = super.getActiveNotifications()
        notifications.forEach { processNotification(it) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let { processNotification(it) }
        super.onNotificationPosted(sbn)
    }

    private fun processNotification(sbn: StatusBarNotification) {
        if (sbn.packageName != Wallets.GOOGLE) return
        val extras = sbn.notification.extras
        val text = extras.get("android.text")
        val tokens = text.toString().split(" with ")
        val place = extras.get("android.title").toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        val priceRaw = tokens[0]
        val price = priceRaw.filter { c -> c.isDigit() || c == '.' || c == ',' }.toDouble().roundToInt()
        val cardLabel = tokens[1]
//        makePayment(price.toDouble(), cardLabel, place)
    }

    private fun makePayment(price: Double, cardLabel: String, item: String) {
        Log.d("Payment", "Spent $price RSD using card: '$cardLabel' for: $item")
        serviceScope.launch {
            useCases.add(RecommendedFinanceItem(
                placeName = item,
                accountLabel = cardLabel,
                amount = price,
                currencyStr = "RSD", // TODO Parse the currency from google wallet notification
                timestamp = System.currentTimeMillis()
            ))
        }
    }
}