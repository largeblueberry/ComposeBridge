package com.largeblueberry.composebridge.domain.model

data class UiComponentModel(
    val id: String,
    val type: String,
    var value: String, // 사용자의 입력을 받는 변수
    val label: String? = null
)
