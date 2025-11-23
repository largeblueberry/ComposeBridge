package com.largeblueberry.dynamicdetail.ui

import com.largeblueberry.dynamicdetail.data.UiStyleConfig

data class DynamicDetailUiState(
    val targetList: List<String> = emptyList(),
    val currentTargetIndex: Int = 0,
    val currentStyle: UiStyleConfig? = null,
    val isStampVisible: Boolean = false
)
