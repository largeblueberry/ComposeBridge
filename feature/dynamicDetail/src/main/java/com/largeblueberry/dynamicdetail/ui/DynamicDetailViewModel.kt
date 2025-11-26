package com.largeblueberry.dynamicdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.data.UiStyleConfig
import com.largeblueberry.data.cart.CartRepository
import com.largeblueberry.dynamicdetail.data.StyleRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface DynamicDetailEffect {
    data class ShareStyleJson(val style: UiStyleConfig) : DynamicDetailEffect
    object NavigateToCart : DynamicDetailEffect // 카트로 이동 이벤트 추가
}

class DynamicDetailViewModel(
    private val repository: StyleRepository = StyleRepository(),
    private val cartRepository: CartRepository = CartRepository() // 카트 레포지터리 추가
) : ViewModel(){

    private val _uiState = MutableStateFlow(DynamicDetailUiState())
    val uiState: StateFlow<DynamicDetailUiState> = _uiState.asStateFlow()

    // Side Effect 처리를 위한 Channel
    private val _effect = Channel<DynamicDetailEffect>()
    val effect = _effect.receiveAsFlow()

    // 현재 스크린 타입 저장
    private var currentScreenType: String = ""

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val targets = repository.getAllTargets()
        val initialStyle = repository.getStyle(targets.first())
        _uiState.update {
            it.copy(
                targetList = targets,
                currentStyle = initialStyle
            )
        }
    }

    // 스크린 타입 설정 (DynamicDetailScreen에서 호출)
    fun setScreenType(screenType: String) {
        currentScreenType = screenType
    }

    // 사용자가 Pager를 스크롤했을 때 호출
    fun onTargetSelected(index: Int) {
        val targets = _uiState.value.targetList
        if (index in targets.indices) {
            val selectedTarget = targets[index]
            val style = repository.getStyle(selectedTarget)
            _uiState.update {
                it.copy(
                    currentTargetIndex = index,
                    currentStyle = style
                )
            }
        }
    }

    // 공유 버튼 클릭 시 호출 (수정됨)
    fun onConfirmClicked() {
        viewModelScope.launch {
            // 1. 스탬프 애니메이션 시작
            _uiState.update { it.copy(isStampVisible = true) }

            // 2. 카트에 저장 (NEW!)
            _uiState.value.currentStyle?.let { style ->
                cartRepository.addToCart(currentScreenType, style)
            }

            // 3. 애니메이션 시간 대기
            delay(1500)

            // 4. 공유 이벤트 발생
            _uiState.value.currentStyle?.let { style ->
                _effect.send(DynamicDetailEffect.ShareStyleJson(style))
            }

            // 5. 스탬프 초기화
            _uiState.update { it.copy(isStampVisible = false) }

            // 6. 잠깐 후 카트로 이동 제안 (선택사항)
            delay(500)
            _effect.send(DynamicDetailEffect.NavigateToCart)
        }
    }
}