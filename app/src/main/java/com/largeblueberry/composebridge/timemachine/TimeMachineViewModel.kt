package com.largeblueberry.composebridge.timemachine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 타임머신 화면의 UI 상태
 */
data class TimeMachineUiState(
    val timeCapsules: List<TimeCapsule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TimeMachineViewModel @Inject constructor(
    private val repository: TimeMachineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimeMachineUiState())
    val uiState: StateFlow<TimeMachineUiState> = _uiState.asStateFlow()

    init {
        loadTimeCapsules()
    }

    /**
     * 타임캡슐 목록을 로드합니다.
     */
    private fun loadTimeCapsules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getAllCapsules().collect { capsules ->
                    _uiState.update {
                        it.copy(
                            timeCapsules = capsules,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    /**
     * 타임캡슐을 새로고침합니다.
     */
    fun refresh() {
        loadTimeCapsules()
    }
}