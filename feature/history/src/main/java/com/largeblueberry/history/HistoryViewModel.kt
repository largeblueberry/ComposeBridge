package com.largeblueberry.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.largeblueberry.data.HistoryDao
import com.largeblueberry.data.HistoryEntity
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDao: HistoryDao) : ViewModel() {

    // DB의 흐름을 바로 StateFlow로 변환
    val historyList = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 스냅샷 저장 (Stamp 버튼 누를 때 호출)
    fun saveSnapshot(currentConfig: UiStyleConfig) {
        viewModelScope.launch {
            val entity = HistoryEntity(
                title = "Design #${System.currentTimeMillis().toString().takeLast(4)}", // 간단한 자동 네이밍
                jsonConfig = currentConfig.toJsonString()
            )
            historyDao.insertHistory(entity)
        }
    }

    // 복원 로직
    fun restoreConfig(entity: HistoryEntity): UiStyleConfig {
        return UiStyleConfig.fromJson(entity.jsonConfig)
    }
}
