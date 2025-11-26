package com.largeblueberry.dynamicdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.data.UiStyleConfig
import com.largeblueberry.dynamicdetail.data.StyleRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


sealed interface DynamicDetailEffect {
    data class ShareStyleJson(val style: UiStyleConfig) : DynamicDetailEffect
}

class DynamicDetailViewModel(
    private val repository: StyleRepository = StyleRepository()
) : ViewModel(){

    private val _uiState = MutableStateFlow(DynamicDetailUiState())
    val uiState: StateFlow<DynamicDetailUiState> = _uiState.asStateFlow()

    // Side Effect 처리를 위한 Channel
    private val _effect = Channel<DynamicDetailEffect>()
    val effect = _effect.receiveAsFlow()

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

    // 공유 버튼 클릭 시 호출
    fun onConfirmClicked() {
        viewModelScope.launch {
            // 1. 스탬프 애니메이션 시작
            _uiState.update { it.copy(isStampVisible = true) }

            // 2. 애니메이션 시간 대기 (비즈니스 로직 상의 대기 시간)
            delay(1500)

            // 3. 공유 이벤트 발생 (UI에게 위임)
            _uiState.value.currentStyle?.let { style ->
                _effect.send(DynamicDetailEffect.ShareStyleJson(style))
            }

            // 4. 스탬프 초기화
            _uiState.update { it.copy(isStampVisible = false) }
        }
    }

}