package com.largeblueberry.data.cart

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.largeblueberry.data.UiStyleConfig

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val screenType: String,
    val styleConfigJson: String, // JSON 문자열로 저장
    val timestamp: Long
) {
    // UiStyleConfig를 JSON으로 변환하는 생성자
    constructor(
        screenType: String,
        styleConfig: UiStyleConfig,
        timestamp: Long
    ) : this(
        id = 0,
        screenType = screenType,
        styleConfigJson = styleConfig.toJsonString(),
        timestamp = timestamp
    )

    // JSON에서 UiStyleConfig로 변환
    val styleConfig: UiStyleConfig
        get() = UiStyleConfig.fromJson(styleConfigJson)
}