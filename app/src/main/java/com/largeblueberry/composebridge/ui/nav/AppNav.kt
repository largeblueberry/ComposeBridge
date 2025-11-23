package com.largeblueberry.composebridge.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.largeblueberry.composebridge.dynamicDetail.DynamicDetailScreen
import com.largeblueberry.composebridge.market.MarketScreen
import com.largeblueberry.composebridge.ui.MainScreen

// --- 1. App Navigation (The Backbone) ---

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
            // 다음 단계에서 만들 DynamicDetailScreen을 여기에 연결할 예정
            DynamicDetailScreen(screenType = screenType)
        }
    }
}