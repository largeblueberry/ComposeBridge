package com.largeblueberry.composebridge.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.largeblueberry.composebridge.market.MarketScreen
import com.largeblueberry.composebridge.ui.MainScreen
import com.largeblueberry.dynamicdetail.ui.DynamicDetailScreen


object Routes {
    const val HOME = "home"
    const val MARKET = "market"
    const val DETAIL = "detail/{screenType}"

    fun createDetailRoute(screenType: String) = "detail/$screenType"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        // Step 1: Main Screen
        composable(Routes.HOME) {
            MainScreen(
                onNavigateToMarket = { navController.navigate(Routes.MARKET) }
            )
        }

        // Step 2: Market Screen (List)
        composable(Routes.MARKET) {
            MarketScreen(
                onNavigateBack = { navController.popBackStack() },
                onItemClick = { type ->
                    navController.navigate(Routes.createDetailRoute(type))
                }
            )
        }

        // Step 3~5: Detail Screen (Placeholder for now)
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("screenType") { type = NavType.StringType })
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType") ?: "Unknown"
            DynamicDetailScreen(screenType)
        }
    }
}