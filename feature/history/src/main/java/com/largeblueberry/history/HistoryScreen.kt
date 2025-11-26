package com.largeblueberry.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.HistoryEntity
import com.largeblueberry.data.UiStyleConfig
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    historyList: List<HistoryEntity>,
    onRestoreClick: (UiStyleConfig) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // 연한 회색 배경
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Time Machine 🕒", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) {
                Text("❌", fontSize = 20.sp)
            }
        }

        // List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(historyList) { history ->
                HistoryItemCard(history, onRestoreClick)
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    history: HistoryEntity,
    onRestoreClick: (UiStyleConfig) -> Unit
) {
    // JSON을 미리 파싱해서 색상 추출 (UI 표시용)
    val previewConfig = remember(history.jsonConfig) {
        UiStyleConfig.fromJson(history.jsonConfig)
    }
    val dateFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRestoreClick(previewConfig) } // 클릭 시 복원
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Visual Preview (색상 팔레트 원형 표시)
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                    ColorDot(previewConfig.primaryColor)
                    ColorDot(previewConfig.secondaryColor)
                    ColorDot(previewConfig.backgroundColor)
                }
            }

            // 2. Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = history.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = dateFormat.format(Date(history.timestamp)),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // 3. Restore Button
            Button(
                onClick = { onRestoreClick(previewConfig) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Load", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ColorDot(color: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color.White, CircleShape)
    )
}
