package com.theminimalismhub.moneymanagement.feature_finances.domain.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddEditFinanceUseCases
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.PaymentListenerUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class PaymentNotificationListener : NotificationListenerService() {

    @Inject
    lateinit var useCases: PaymentListenerUseCases

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private object Wallets {
        const val GOOGLE = "com.google.android.apps.walletnfcrel"
        const val DEBUG = "com.samsung.android.app.smartcapture"
    }

//    override fun onListenerConnected() {
//        super.onListenerConnected()
//        val notifications = super.getActiveNotifications()
//        notifications.forEach { processNotification(it) }
//    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let { processNotification(it) }
        super.onNotificationPosted(sbn)
    }

    private fun processNotification(sbn: StatusBarNotification) {
//        if (sbn.packageName == Wallets.DEBUG) makePayment(parseLocalizedPrice("3,777"), "The VISA", "Univerexport")
        if (sbn.packageName != Wallets.GOOGLE) return
        val extras = sbn.notification.extras
        val text = extras.get("android.text")
        val tokens = text.toString().split(" with ")
        val place = extras.get("android.title").toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        val priceRaw = tokens[0]
        val price = parseLocalizedPrice(priceRaw)
        val cardLabel = tokens[1]
        makePayment(price, cardLabel, place)
        super.cancelNotification(sbn.key)
    }

    fun parseLocalizedPrice(price: String): Double? {

        val cleanPrice = price.replace("\\s".toRegex(), "").filter { c -> c.isDigit() || c == '.' || c == ',' }

        return try {
            val normalized = when {
                // Format like 1,000.00
                cleanPrice.contains(",") && cleanPrice.contains(".") && cleanPrice.indexOf(",") < cleanPrice.indexOf(".") -> {
                    cleanPrice.replace(",", "")
                }
                // Format like 1.000,00
                cleanPrice.contains(".") && cleanPrice.contains(",") &&
                        cleanPrice.indexOf(".") < cleanPrice.indexOf(",") -> {
                    cleanPrice.replace(".", "").replace(",", ".")
                }
                // Format like 3,459
                cleanPrice.contains(",") -> {
                    cleanPrice.replace(",", "")
                }
                // Format like 3459
                cleanPrice.matches("\\d+".toRegex()) -> {
                    cleanPrice
                }
                else -> cleanPrice
            }
            normalized.toDouble()
        } catch (e: Exception) {
            null
        }
    }


    private fun makePayment(price: Double?, cardLabel: String, item: String) {
        Log.d("Payment", "Spent $price RSD using card: '$cardLabel' for: $item")
        serviceScope.launch {
            val accounts: List<Account> = useCases.getAccounts().first()
            val account: Account? = accounts.firstOrNull { account ->
                account.labels.split(",")
                    .map { it.trim().lowercase() }
                    .contains(cardLabel.trim().lowercase())
                && !account.deleted
            }
            useCases.addFinance(RecommendedFinanceItem(
                placeName = item,
                accountLabel = cardLabel,
                amount = price ?: 0.0,
                currencyStr = "RSD", // TODO Parse the currency from google wallet notification
                timestamp = System.currentTimeMillis(),
                financeAccountId = account?.accountId
            ))
        }
    }
}