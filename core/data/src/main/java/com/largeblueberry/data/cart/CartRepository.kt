package com.largeblueberry.data.cart

import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems().map { entities ->
        entities.map { entity ->
            CartItem(
                id = entity.id,
                screenType = entity.screenType,
                styleConfig = entity.styleConfig, // getter 사용
                timestamp = entity.timestamp
            )
        }
    }

    // 카트에 아이템 추가
    suspend fun addToCart(screenType: String, styleConfig: UiStyleConfig) {
        // 중복 체크: 같은 screenType + targetName 조합이 있는지 확인
        val existingItem = cartDao.findByScreenTypeAndTarget(screenType, styleConfig.targetName)

        if (existingItem != null) {
            // 기존 아이템 업데이트 (타임스탬프와 스타일 갱신)
            val updatedEntity = CartEntity(
                id = existingItem.id,
                screenType = screenType,
                styleConfigJson = styleConfig.toJsonString(),
                timestamp = System.currentTimeMillis()
            )
            cartDao.updateCartItem(updatedEntity)
        } else {
            // 새 아이템 추가
            val newEntity = CartEntity(
                screenType = screenType,
                styleConfig = styleConfig,
                timestamp = System.currentTimeMillis()
            )
            cartDao.insertCartItem(newEntity)
        }
    }

    // 카트에서 아이템 제거
    suspend fun removeFromCart(itemId: Int) {
        cartDao.deleteCartItem(itemId)
    }

    // 카트 비우기
    suspend fun clearCart() {
        cartDao.deleteAllCartItems()
    }

    // 카트 아이템 개수
    suspend fun getCartItemCount(): Int {
        return cartDao.getCartItemCount()
    }

    // 특정 아이템 존재 여부 확인
    suspend fun isItemInCart(screenType: String, targetName: String): Boolean {
        return cartDao.findByScreenTypeAndTarget(screenType, targetName) != null
    }
}

// 카트 아이템 데이터 클래스 (기존과 동일)
data class CartItem(
    val id: Int,
    val screenType: String,
    val styleConfig: UiStyleConfig,
    val timestamp: Long
) {
    val templateName: String
        get() = screenType

    // 표시용 이름 생성
    fun getDisplayName(): String {
        return "${screenType.replaceFirstChar { it.uppercase() }} UI - ${styleConfig.targetName}"
    }
}