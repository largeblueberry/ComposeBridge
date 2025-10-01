package com.largeblueberry.composebridge.model

data class UiComponentModel(
    val id: String,
    val type: String,
    var value: String, // 사용자가 수정할 수 있도록 var로 선언
    val label: String? = null
)
