package com.theminimalismhub.moneymanagement

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.NavGraphs
import com.theminimalismhub.moneymanagement.ui.theme.MoneyManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterialNavigationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                insets.left,
                0,
                insets.right,
                insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration);

        setContent {
            MoneyManagementTheme {
                val engine = rememberAnimatedNavHostEngine()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root, engine = engine)
                }
            }
        }
    }
    private fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 0.875f) {
            configuration.fontScale = 0.875f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    override fun attachBaseContext(baseContext: Context) {
        val newContext: Context
        val displayMetrics: DisplayMetrics = baseContext.getResources().getDisplayMetrics()
        val configuration: Configuration = baseContext.getResources().getConfiguration()
        if (displayMetrics.widthPixels / displayMetrics.densityDpi < 2.65f) {
            configuration.densityDpi = (displayMetrics.widthPixels / 2.65f).toInt()
            newContext = baseContext.createConfigurationContext(configuration)
        } else {
            newContext = baseContext
        }
        super.attachBaseContext(newContext)
    }
}