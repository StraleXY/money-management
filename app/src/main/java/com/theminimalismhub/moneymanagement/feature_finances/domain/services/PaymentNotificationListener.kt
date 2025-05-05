package com.theminimalismhub.moneymanagement.feature_finances.domain.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class PaymentNotificationListener : NotificationListenerService() {

    object Wallets {
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
        if(sbn.packageName != Wallets.GOOGLE) return
        val extras = sbn.notification.extras
        val title = extras.get("android.title")
        val text = extras.get("android.text")
        Log.d("Notification", "Pckg: ${sbn.packageName } | Title: $title | Text: $text")
    }
}