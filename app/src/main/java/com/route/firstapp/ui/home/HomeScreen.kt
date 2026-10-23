package com.route.firstapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.route.domain.data.ProductsSort
import com.route.firstapp.AppNavHost
import com.route.firstapp.R
import com.route.firstapp.ui.AppRoutes
import com.route.firstapp.ui.AppSearchBar
import com.route.firstapp.ui.categories.CategoriesScreen
import com.route.firstapp.ui.home.categoriesSection.HomeCategoriesSection
import com.route.firstapp.ui.home.productsSection.HorizontalProductCardRow
import com.route.firstapp.ui.productDetails.ProductDetailsScreen
import com.route.firstapp.ui.theme.FirstAppTheme

enum class HomeTabs(
    val icon: ImageVector,
    val route: AppRoutes, ){
    HOME(icon = Icons.Outlined.Home,route = AppRoutes.HomeTab),
    CATEGORIES(icon = Icons.Outlined.Face,route = AppRoutes.CategoriesTab),
    FAVORITES(icon = Icons.Outlined.Favorite,route = AppRoutes.FavoritesTab),
    PROFILE(icon = Icons.Outlined.Person,route = AppRoutes.ProfileTab),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               appNavController: NavController,
               ) {
    val navController = rememberNavController()
    // Home's own back stack entry in AppNavHost, it stops being RESUMED once a navigation starts
    val lifecycleOwner = LocalLifecycleOwner.current
    // read the selected tab from the back stack so it stays right after system back
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    Scaffold(
        contentColor = Color.White,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                HomeTabs.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute(destination.route::class) == true,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                // one entry per tab: pop back to the start tab and keep each tab's state
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = "",
                            )
                        },
                    )
                }
            }
        },
        topBar = {
            TopAppBar(title = {
                Image(
                    modifier = Modifier.height(24.dp),
                    painter = painterResource(R.drawable.route_logo),
                    contentDescription = stringResource(R.string.route_logo),
                    contentScale = ContentScale.FillHeight
                )
            }
            )
        }
    ){ innerPadding ->
        HomeNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.HomeTab,
            onNavigate = { destination ->
                // full-screen destinations live in AppNavHost, so use the root controller.
                // A second tap during the transition would push the destination twice.
                if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    appNavController.navigate(destination)
                }
            },
        )
    }

}



@Composable
fun HomeNavHost(modifier: Modifier = Modifier,
               navController: NavHostController,
               startDestination: AppRoutes,
               onNavigate: (AppRoutes) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<AppRoutes.HomeTab> {
            HomeTab(
                onNavigate = onNavigate,
            )
        }
        composable<AppRoutes.FavoritesTab> {
            FavoritesTab()
        }
        composable<AppRoutes.CategoriesTab> {
            CategoriesTab(
                onNavigate = onNavigate,
            )
        }
        composable<AppRoutes.ProfileTab> {
            ProfileTab()
        }
    }

}
@Composable
fun HomeTab(modifier: Modifier = Modifier,
            onNavigate: (AppRoutes) -> Unit) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState())
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        AppSearchBar()
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(R.drawable.home_banner),
            contentDescription = "home banner"
        )


        HomeCategoriesSection()

        HorizontalProductCardRow(
            sort = ProductsSort.MOST_SELLING,
            onNavigate = onNavigate,
        )

        HorizontalProductCardRow(
            sort = ProductsSort.LOWEST_PRICE,
            onNavigate = onNavigate,
        )

        HorizontalProductCardRow(
            sort = ProductsSort.LATEST_ADDED,
            onNavigate = onNavigate,
        )
    }
}

@Composable
fun CategoriesTab(modifier: Modifier = Modifier,
                  onNavigate: (AppRoutes) -> Unit) {
    CategoriesScreen(
        modifier = modifier,
        onNavigate = onNavigate,
    )
}

@Composable
fun FavoritesTab(modifier: Modifier = Modifier) {

}

@Composable
fun ProfileTab(modifier: Modifier = Modifier) {

}
