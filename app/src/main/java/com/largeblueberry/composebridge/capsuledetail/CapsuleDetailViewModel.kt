package com.largeblueberry.composebridge.capsuledetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.composebridge.timemachine.GeneratePdfUseCase
import com.largeblueberry.composebridge.timemachine.TimeCapsule
import com.largeblueberry.composebridge.timemachine.TimeMachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CapsuleDetailUiState(
    val capsule: TimeCapsule? = null,
    val isLoading: Boolean = false,
    val isExporting: Boolean = false,
    val snackbarMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class CapsuleDetailViewModel @Inject constructor(
    private val generatePdfUseCase: GeneratePdfUseCase,
    private val repository: TimeMachineRepository,
    @ApplicationContext private val applicationContext: Context,  // ✅ ApplicationContext 주입
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val capsuleId: String = checkNotNull(savedStateHandle["capsuleId"])

    private val _uiState = MutableStateFlow(CapsuleDetailUiState())
    val uiState: StateFlow<CapsuleDetailUiState> = _uiState.asStateFlow()

    init {
        loadCapsule()
    }

    private fun loadCapsule() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getCapsuleById(capsuleId).collect { capsule ->
                    _uiState.update {
                        it.copy(
                            capsule = capsule,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    // ✅ Context를 파라미터로 받음
    fun exportAndSharePdf(activityContext: Context) {  // ✅ Activity Context 전달받음
        val capsule = _uiState.value.capsule ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true) }

            generatePdfUseCase.executeAndShare(activityContext, capsule)  // ✅ context 전달
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            snackbarMessage = "PDF가 생성되었습니다."
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}