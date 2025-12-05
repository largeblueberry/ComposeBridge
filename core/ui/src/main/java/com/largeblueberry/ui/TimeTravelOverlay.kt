package com.largeblueberry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimeTravelOverlay() {
    // 풀스크린 오버레이
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)), // 어두운 배경
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 간단한 로딩 인디케이터 (실제로는 더 멋진 애니메이션을 넣을 수 있습니다)
            CircularProgressIndicator(
                color = PrimaryBlue,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "시간 여행 중...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "선택된 버전으로 전환 중입니다.",
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}
