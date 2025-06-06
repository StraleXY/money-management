package com.theminimalismhub.moneymanagement.feature_settings.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.di.MoneyDatabase
import com.theminimalismhub.moneymanagement.di.path
import com.theminimalismhub.moneymanagement.di.query
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ToggleTracking
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.HomeEvent
import com.theminimalismhub.moneymanagement.feature_settings.composables.SettingsSegment
import com.theminimalismhub.moneymanagement.feature_settings.composables.SettingsTile
import com.theminimalismhub.moneymanagement.feature_settings.domain.Preferences
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

@Composable
@Destination(style = BaseTransition::class)
fun SettingsScreen(
    navigator: DestinationsNavigator,
    vm: SettingsViewModel = hiltViewModel()
) {
    val state = vm.state.value
    val context = LocalContext.current
    val backupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/vnd.sqlite3")) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult

            MoneyDatabase.Instance?.close()
            query {
                context.applicationContext.contentResolver.openOutputStream(uri)
                    ?.use { outputStream ->
                        FileInputStream(context.getDatabasePath(MoneyDatabase.DATABASE_NAME)).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                exitProcess(0)
            }
        }

    val restoreLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult

            query {
                MoneyDatabase.Instance?.close()

                context.applicationContext.contentResolver.openInputStream(uri)
                    ?.use { inputStream ->
                        FileOutputStream(MoneyDatabase.Instance?.path).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                exitProcess(0)
            }
        }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            ScreenHeader(
                title = "Settings",
                hint = "This is the place where you can backup your database and manage your licence."
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingsSegment(name = "GENERAL")
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Currency",
                description = "Change the display currency trough-out the app; Please reset the app to ensure that the changes take effect.",
                fieldState = vm.formState.getState("currency"),
                inputType = KeyboardType.Text,
                onValChanged = { vm.onEvent(SettingsEvent.OnCurrencyChanged(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Show Line Graph",
                description = "Toggle whether weekly and monthly line graphs are shown.",
                toggled = state.showLineGraph,
                onToggle = { vm.onEvent(SettingsEvent.ToggleShowLineGraph) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Collapsable Categories",
                description = "Collapse category bars by default to 5 entries.",
                toggled = state.collapseCategories,
                onToggle = { vm.onEvent(SettingsEvent.ToggleCollapseCategories) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Filter Income",
                description = "Allows you to filter income by account.",
                toggled = state.filterIncomeByAccount,
                onToggle = { vm.onEvent(SettingsEvent.ToggleFilterIncomeByAccount) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Filter Outcome",
                description = "Allows you to filter outcome by account.",
                toggled = state.filterOutcomeByAccount,
                onToggle = { vm.onEvent(SettingsEvent.ToggleFilterOutcomeByAccount) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Swipeable Navigation",
                description = "Enables swiping on the spending's card for movement through time instead of arrows above the card.",
                toggled = state.swipeableNavigation,
                onToggle = { vm.onEvent(SettingsEvent.ToggleSwipeableNavigation) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSegment(name = "LIMITS")
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Daily Limit",
                description = "After updating the value, please reset the app to ensure that the changes take effect.",
                fieldState = vm.formState.getState("limit"),
                inputType = KeyboardType.Number,
                onValChanged = { vm.onEvent(SettingsEvent.OnDailyLimitChanged(it)) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSegment(name = "DATABASE")
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTile(
                title = "Backup",
                icon = Icons.Rounded.UploadFile,
                description = "Export the database to the external storage\n" +
                        "App will automatically close after backup is complete.",
                onClick = {
                    @SuppressLint("SimpleDateFormat")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                    try {
                        backupLauncher.launch("MM_${dateFormat.format(Date())}.db")
                    } catch (e: ActivityNotFoundException) {
                        Log.d("DB", "Couldn't find an application to create documents")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingsTile(
                title = "Restore",
                icon = Icons.Default.RestorePage,
                description = "Existing data will be overwritten.\n" +
                        "App will automatically close after restore is complete.",
                descriptionColor = MaterialTheme.colors.error,
                onClick = {
                    try {
                        restoreLauncher.launch(
                            arrayOf(
                                "application/vnd.sqlite3",
                                "application/x-sqlite3",
                                "application/octet-stream"
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        Log.d("DB", "Couldn't find an application to open documents")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(context, intent, null)
            }) {
                Text(text = "Request Notificaton Access")
            }
        }
    }

}