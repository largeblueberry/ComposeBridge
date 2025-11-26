package com.largeblueberry.data.cart

import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// MVP용 간단한 CartRepository (Room 연결 전 단계)
class CartRepository {

    // 임시로 메모리에 저장 (나중에 Room으로 교체)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: Flow<List<CartItem>> = _cartItems.asStateFlow()

    // 카트에 아이템 추가
    suspend fun addToCart(screenType: String, styleConfig: UiStyleConfig) {
        val newItem = CartItem(
            id = System.currentTimeMillis().toInt(), // 임시 ID
            screenType = screenType,
            styleConfig = styleConfig,
            timestamp = System.currentTimeMillis()
        )

        val currentList = _cartItems.value.toMutableList()
        currentList.add(newItem)
        _cartItems.value = currentList
    }

    // 카트에서 아이템 제거
    suspend fun removeFromCart(itemId: Int) {
        val currentList = _cartItems.value.toMutableList()
        currentList.removeAll { it.id == itemId }
        _cartItems.value = currentList
    }

    // 카트 비우기
    suspend fun clearCart() {
        _cartItems.value = emptyList()
    }

    // 카트 아이템 개수
    fun getCartItemCount(): Int = _cartItems.value.size
}

// 카트 아이템 데이터 클래스
data class CartItem(
    val id: Int,
    val screenType: String,
    val styleConfig: UiStyleConfig,
    val timestamp: Long
) {
    // 표시용 이름 생성
    fun getDisplayName(): String {
        return "${screenType.replaceFirstChar { it.uppercase() }} UI - ${styleConfig.targetName}"
    }
}