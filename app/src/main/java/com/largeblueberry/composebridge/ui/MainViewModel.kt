package com.largeblueberry.composebridge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 화면의 상태를 나타내는 데이터 클래스
data class MainScreenUiState(
    val screenList: List<String> = emptyList()
)

class MainViewModel(isPreview: Boolean = false) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        if (isPreview) {
            // 미리보기용 더미 데이터 제공
            _uiState.value = MainScreenUiState(screenList = listOf("로그인 (미리보기)", "설정 (미리보기)", "채팅 (미리보기)"))
        } else {
            // 실제 앱에서 사용할 데이터 로딩
            loadScreenList()
        }
    }

    private fun loadScreenList() {
        viewModelScope.launch {
            // TODO: 추후 Room DB 등에서 데이터를 가져오는 로직으로 대체
            val screens = listOf("로그인 화면", "설정 화면", "채팅 화면")
            _uiState.value = _uiState.value.copy(screenList = screens)
        }
    }
}
