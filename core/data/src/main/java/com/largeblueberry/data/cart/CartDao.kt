package com.largeblueberry.data.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items ORDER BY timestamp DESC")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    @Update
    suspend fun updateCartItem(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun deleteCartItem(itemId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems()

    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getCartItemCount(): Int

    @Query("SELECT * FROM cart_items WHERE screenType = :screenType AND styleConfigJson LIKE '%\"target\":\"' || :targetName || '\"%' LIMIT 1")
    suspend fun findByScreenTypeAndTarget(screenType: String, targetName: String): CartEntity?

    @Query("SELECT * FROM cart_items WHERE id = :itemId")
    suspend fun getCartItemById(itemId: Int): CartEntity?
}