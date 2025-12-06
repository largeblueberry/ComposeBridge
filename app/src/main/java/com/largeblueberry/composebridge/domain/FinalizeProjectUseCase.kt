package com.largeblueberry.composebridge.domain

import com.largeblueberry.composebridge.timemachine.TimeMachineRepository
import com.largeblueberry.data.cart.CartItem
import com.largeblueberry.data.cart.CartRepository
import javax.inject.Inject

/**
 * 최종 프로젝트 확정(Final Approve) 로직을 캡슐화하는 유즈케이스입니다.
 * 이 유즈케이스는 CartRepository와 TimeMachineRepository에 의존하지만,
 * CartViewModel은 이 유즈케이스만 알면 됩니다.
 */
class FinalizeProjectUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val timeMachineRepository: TimeMachineRepository
) {
    suspend operator fun invoke(projectTitle: String, finalItems: List<CartItem>) {
        if (finalItems.isEmpty()) return

        // 1. 타임머신 저장소에 캡슐로 저장
        timeMachineRepository.saveNewCapsule(projectTitle, finalItems)

        // 2. (TODO: PDF/JSON 추출 로직 호출)
        println("UseCase: Initiating PDF/JSON export for $projectTitle")

        // 3. 장바구니 비우기
        cartRepository.clearCart()
    }
}
