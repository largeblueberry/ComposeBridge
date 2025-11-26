package com.largeblueberry.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,          // 예: "Design #1", "Final V2"
    val jsonConfig: String,     // UiStyleConfig.toJsonString() 결과 저장
    val timestamp: Long = System.currentTimeMillis()
)
