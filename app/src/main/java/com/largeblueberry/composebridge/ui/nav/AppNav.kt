package com.largeblueberry.composebridge.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.largeblueberry.composebridge.capsuledetail.CapsuleDetailScreen
import com.largeblueberry.composebridge.capsuledetail.CapsuleDetailViewModel
import com.largeblueberry.composebridge.market.MarketScreen
import com.largeblueberry.composebridge.timemachine.TimeMachineScreen
import com.largeblueberry.composebridge.timemachine.TimeMachineViewModel
import com.largeblueberry.composebridge.ui.MainScreen
import com.largeblueberry.dynamicdetail.ui.DynamicDetailScreen
import com.largeblueberry.composebridge.cart.CartScreen

object Routes {
    const val HOME = "home"
    const val MARKET = "market"
    const val DETAIL = "detail/{screenType}"
    const val CART = "cart"
    const val TIME_MACHINE = "time_machine"
    const val CAPSULE_DETAIL = "capsule_detail/{capsuleId}"

    fun createDetailRoute(screenType: String) = "detail/$screenType"
    fun createCapsuleDetailRoute(capsuleId: String) = "capsule_detail/$capsuleId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            MainScreen(
                onNavigateToMarket = { navController.navigate(Routes.MARKET) },
                onNavigateToCart = { navController.navigate(Routes.CART) }
            )
        }

        composable(Routes.MARKET) {
            MarketScreen(
                onNavigateBack = { navController.popBackStack() },
                onItemClick = { type ->
                    navController.navigate(Routes.createDetailRoute(type))
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("screenType") { type = NavType.StringType })
        ) { backStackEntry ->
            val screenType = backStackEntry.arguments?.getString("screenType") ?: "Unknown"
            DynamicDetailScreen(
                screenType = screenType,
                onNavigateToCart = {
                    navController.navigate(Routes.CART)
                }
            )
        }

        // Cart Screen 추가
        composable(route = Routes.CART) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onProjectFinalized = {
                    // 프로젝트 확정 후 타임머신으로 이동
                    navController.navigate(Routes.TIME_MACHINE) {
                        // Cart를 백스택에서 제거 (뒤로가기 시 Cart로 돌아가지 않도록)
                        popUpTo(Routes.CART) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.TIME_MACHINE) {
            val timeMachineViewModel: TimeMachineViewModel = hiltViewModel()
            val uiState by timeMachineViewModel.uiState.collectAsState()

            TimeMachineScreen(
                timeCapsules = uiState.timeCapsules,
                isLoading = uiState.isLoading,
                error = uiState.error,
                onBackClick = { navController.popBackStack() },
                onCapsuleClick = { capsule ->
                    navController.navigate(Routes.createCapsuleDetailRoute(capsule.id))
                },
                onRefresh = { timeMachineViewModel.refresh() }
            )
        }

        composable(
            route = Routes.CAPSULE_DETAIL,
            arguments = listOf(navArgument("capsuleId") { type = NavType.StringType })
        ) {
            CapsuleDetailScreen(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}