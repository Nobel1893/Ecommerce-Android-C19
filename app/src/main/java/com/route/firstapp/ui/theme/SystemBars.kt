package com.route.firstapp.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Light status and navigation bar icons while [useLightIcons] is true (screens drawn on BrandColor),
 * the theme's default icons otherwise. enableEdgeToEdge() only picks them from the light/dark theme.
 * It draws nothing, so it has no preview; previews run without an Activity and skip it.
 */
@Composable
fun SystemBarIconsEffect(useLightIcons: Boolean) {
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    LaunchedEffect(view, useLightIcons, isDarkTheme) {
        val window = view.context.findActivity()?.window ?: return@LaunchedEffect
        val useDarkIcons = !useLightIcons && !isDarkTheme
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = useDarkIcons
            isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
