package com.theminimalismhub.moneymanagement.core.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.theminimalismhub.moneymanagement.appDestination
import com.theminimalismhub.moneymanagement.destinations.HomeScreenDestination

@OptIn(ExperimentalAnimationApi::class)
object AddEditScreenTransition : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return slideInVertically(
            initialOffsetY = { height -> height },
            animationSpec = tween(350)
        ) + fadeIn(animationSpec = tween(150))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { height -> height },
            animationSpec = tween(350)
        ) + fadeOut(animationSpec = tween(350))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideInVertically(
            initialOffsetY = { height -> height },
            animationSpec = tween(350)
        ) + fadeIn(animationSpec = tween(150))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { height -> height },
            animationSpec = tween(350)
        ) + fadeOut(animationSpec = tween(350))
    }
}

@OptIn(ExperimentalAnimationApi::class)
object BaseTransition : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
//        if(targetState.appDestination() == HomeScreenDestination || initialState.appDestination() == ActivationScreenDestination) return getEnterTransition(0.95f)
        return getEnterTransition(1.05f)
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return when (targetState.appDestination()) {
            HomeScreenDestination -> getExitTransition(1.05f)
            else -> getExitTransition(0.95f)
        }
    }
    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
//        if(targetState.appDestination() == HomeScreenDestination || initialState.appDestination() == ActivationScreenDestination) return getEnterTransition(0.95f)
        return getEnterTransition(1.05f)
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return when (targetState.appDestination()) {
            HomeScreenDestination -> getExitTransition(1.05f)
            else -> getExitTransition(0.95f)
        }
    }

    private fun getEnterTransition(initialScale: Float): EnterTransition {
        return scaleIn(
            initialScale = initialScale,
            animationSpec = tween(450)
        ) + fadeIn(animationSpec = tween(350))
    }

    private fun getExitTransition(targetScale: Float): ExitTransition {
        return scaleOut(
            targetScale = targetScale,
            animationSpec = tween(350)
        ) + fadeOut(animationSpec = tween(450))
    }
}