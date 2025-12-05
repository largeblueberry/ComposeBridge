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
import com.largeblueberry.composebridge.cart.CartScreen
import com.largeblueberry.composebridge.cart.CartViewModel
import com.largeblueberry.composebridge.market.MarketScreen
import com.largeblueberry.composebridge.timemachine.TimeMachineScreen
import com.largeblueberry.composebridge.timemachine.TimeMachineViewModel
import com.largeblueberry.composebridge.ui.MainScreen
import com.largeblueberry.dynamicdetail.ui.DynamicDetailScreen

object Routes {
    const val HOME = "home"
    const val MARKET = "market"
    const val DETAIL = "detail/{screenType}"
    const val CART = "cart"
    const val TIME_MACHINE = "time_machine"

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
            // 각 화면에 필요한 ViewModel을 주입받습니다.
            val cartViewModel: CartViewModel = hiltViewModel()
            // TimeMachineViewModel은 CartViewModel에서 간접적으로 처리하므로 여기서는 필요하지 않습니다.

            CartScreen(
                onBackClick = { navController.popBackStack() },
                // onCheckoutClick 매개변수는 CartScreen에서 제거되었으므로 삭제합니다.
                // 최종 확정 로직은 CartScreen 내부에서 ViewModel 함수를 호출하도록 변경되었습니다.

                // TODO: 만약 최종 확정 후 네비게이션이 필요하다면, CartScreen에
                // onProjectFinalized: () -> Unit 콜백을 추가하고 여기서 호출해야 합니다.
                // 현재는 CartViewModel.finalApproveProject가 호출되는 것으로 충분합니다.
            )
        }

        composable(route = Routes.TIME_MACHINE) {
            // TimeMachineScreen에 필요한 ViewModel을 주입받습니다.
            val timeMachineViewModel: TimeMachineViewModel = hiltViewModel()
            val capsules by timeMachineViewModel.timeCapsules.collectAsState()

            TimeMachineScreen(
                timeCapsules = capsules, // ViewModel의 상태를 전달합니다.
                onBackClick = { navController.popBackStack() },
                onCapsuleClick = { capsule ->
                    // TODO: 다음 단계에서 상세 화면으로 이동하는 로직 구현
                    // 예: navController.navigate("stampDetail/${capsule.id}")
                }
            )
        }
    }

}