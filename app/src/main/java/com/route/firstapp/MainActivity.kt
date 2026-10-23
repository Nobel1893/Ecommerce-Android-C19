package com.route.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.route.firstapp.ui.AppRoutes
import com.route.firstapp.ui.auth.AuthBackground
import com.route.firstapp.ui.auth.login.LoginScreen
import com.route.firstapp.ui.auth.register.RegisterScreen
import com.route.firstapp.ui.home.HomeScreen
import com.route.firstapp.ui.productDetails.ProductDetailsScreen
import com.route.firstapp.ui.productsCatalog.ProductsCatalogScreen
import com.route.firstapp.ui.session.SessionContract
import com.route.firstapp.ui.session.SessionViewModel
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.FirstAppTheme
import com.route.firstapp.ui.theme.SystemBarIconsEffect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RouteEcommerceApp()
        }
    }
}

@Composable
fun RouteEcommerceApp(sessionViewModel: SessionViewModel = hiltViewModel()){
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        sessionViewModel.doIntent(SessionContract.Intent.Init)
    }
    val sessionState = sessionViewModel.states.collectAsStateWithLifecycle()
    // 1. Create and remember the NavController
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isOnBrandColor = currentBackStackEntry?.destination.isDrawnOnBrandColor()

    FirstAppTheme {
        SystemBarIconsEffect(useLightIcons = isOnBrandColor)
        // This colour also shows through the NavHost's cross-fade, so it follows the destination:
        // BrandColor keeps Login <-> Register from flashing lighter halfway through.
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isOnBrandColor) BrandColor else MaterialTheme.colorScheme.background,
            contentColor = Color.White,
        ) {
            when (sessionState.value) {
                // the saved login is read off the main thread; only the brand background shows meanwhile
                SessionContract.State.Loading -> AuthBackground()
                SessionContract.State.LoggedIn -> AppNavHost(
                    navController = navController,
                    startDestination = AppRoutes.Home
                )
                SessionContract.State.LoggedOut -> AppNavHost(
                    navController = navController,
                    startDestination = AppRoutes.Login
                )
            }
        }
    }

}

@Composable
fun AppNavHost(modifier: Modifier = Modifier,
               navController: NavHostController,
               startDestination: AppRoutes
               ) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<AppRoutes.Login> {
            LoginScreen(
                onNavigate = { route -> navController.navigateFromAuth(route) }
            )
        }
        composable<AppRoutes.Register> {
            RegisterScreen(
                onNavigate = { route -> navController.navigateFromAuth(route) }
            )
        }
        composable<AppRoutes.Home> {
            HomeScreen(
                appNavController = navController,
            )
        }
        composable<AppRoutes.ProductDetails> {
            ProductDetailsScreen()
        }
        composable<AppRoutes.ProductsCatalog> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoutes.ProductsCatalog>()
            ProductsCatalogScreen(
                categoryId = route.categoryId,
                subCategoryId = route.subCategoryId,
            )
        }

    }

}

// Reaching Home ends the auth flow: Login and Register leave the back stack, so Back from Home exits
private fun NavController.navigateFromAuth(route: AppRoutes) {
    navigate(route) {
        launchSingleTop = true
        if (route == AppRoutes.Home) {
            popUpTo<AppRoutes.Login> { inclusive = true }
        }
    }
}

// null: the session is still loading and only the brand background is on screen
private fun NavDestination?.isDrawnOnBrandColor(): Boolean {
    return this == null || hasRoute<AppRoutes.Login>() || hasRoute<AppRoutes.Register>()
}
