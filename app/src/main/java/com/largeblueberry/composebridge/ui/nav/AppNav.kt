package com.largeblueberry.composebridge.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.largeblueberry.composebridge.cart.CartScreen
import com.largeblueberry.composebridge.market.MarketScreen
import com.largeblueberry.composebridge.ui.MainScreen
import com.largeblueberry.dynamicdetail.ui.DynamicDetailScreen

object Routes {
    const val HOME = "home"
    const val MARKET = "market"
    const val DETAIL = "detail/{screenType}"
    const val CART = "cart"

    fun createDetailRoute(screenType: String) = "detail/$screenType"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        // Step 1: Main Screen
        composable(Routes.HOME) {
            MainScreen(
                onNavigateToMarket = { navController.navigate(Routes.MARKET) },
                onNavigateToCart = { navController.navigate(Routes.CART) }
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

        // Step 3~5: Detail Screen (수정됨 - 카트 네비게이션 추가)
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("screenType") { type = NavType.StringType })
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType") ?: "Unknown"
            DynamicDetailScreen(
                screenType = screenType,
                onNavigateToCart = {
                    // 카트로 이동
                    navController.navigate(Routes.CART)
                }
            )
        }

        composable(route = Routes.CART) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = {
                    // TODO: 주문 확정 화면으로 이동 (다음 단계에서 구현)
                }
            )
        }
    }
}