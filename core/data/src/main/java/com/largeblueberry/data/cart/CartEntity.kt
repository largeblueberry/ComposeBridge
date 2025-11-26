package com.largeblueberry.data.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val screenType: String,
    val styleConfigJson: String,
    val timestamp: Long = System.currentTimeMillis()
)
