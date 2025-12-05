package com.largeblueberry.composebridge.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.data.cart.CartItem
import com.largeblueberry.data.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    /**
     * 최종 확정된 프로젝트를 승인하고, PDF/JSON 생성 및 타임머신 저장소에 등록하는 로직을 시작합니다.
     * @param projectTitle 사용자가 입력한 프로젝트의 최종 제목
     */
    fun finalApproveProject(projectTitle: String) {
        viewModelScope.launch {
            // 1. 현재 장바구니에 있는 최종 확정 아이템 목록을 가져옵니다.
            val finalItems = cartItems.value

            // 2. 핵심 로직 수행:
            // - PDF 추출 기능 구현 (화면 + JSON 데이터)
            // - JSON 데이터 추출 기능 구현 (개발자용 구조화된 데이터)
            // - 타임머신(버전 관리) 저장소에 최종 버전으로 등록

            // TODO: cartRepository 또는 별도의 SpecGeneratorRepository를 통해
            // 실제 PDF/JSON 생성 및 저장소 등록 함수를 호출해야 합니다.
            // 예: cartRepository.exportAndSaveProject(projectTitle, finalItems)

            println("Project Finalized: $projectTitle")
            println("Items count for export: ${finalItems.size}")

            // 3. 최종 승인이 완료되었으므로 장바구니를 비웁니다.
            cartRepository.clearCart()
        }
    }
}
