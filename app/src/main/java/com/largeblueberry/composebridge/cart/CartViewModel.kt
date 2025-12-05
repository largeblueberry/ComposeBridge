package com.largeblueberry.composebridge.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.composebridge.domain.FinalizeProjectUseCase
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
    private val cartRepository: CartRepository,
    private val finalizeProjectUseCase: FinalizeProjectUseCase
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
     * 최종 확정 로직을 실행합니다.
     * CartViewModel은 내부에서 어떤 일이 일어나는지(타임머신 저장, 장바구니 비우기 등) 알 필요 없이
     * 유즈케이스에 위임합니다.
     * @param projectTitle 사용자가 입력한 프로젝트의 최종 제목
     */
    fun finalApproveProject(projectTitle: String) {
        viewModelScope.launch {
            val finalItems = cartItems.value

            if (finalItems.isNotEmpty()) {
                // 유즈케이스 호출
                finalizeProjectUseCase(projectTitle, finalItems)
            } else {
                println("Error: Cannot finalize an empty cart.")
            }
        }
    }
}
