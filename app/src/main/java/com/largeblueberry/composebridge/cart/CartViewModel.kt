package com.largeblueberry.composebridge.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.data.cart.CartItem
import com.largeblueberry.data.cart.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {

    // 카트 아이템들을 StateFlow로 노출
    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 카트에서 아이템 제거
    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }

    // 카트 비우기
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    // 주문 확정 (다음 단계에서 구현)
    fun proceedToCheckout() {
        // TODO: 주문 확정 로직
    }
}