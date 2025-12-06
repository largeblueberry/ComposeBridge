package com.largeblueberry.dynamicdetail.ui.component.template

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun RecordTemplate(
    style: UiStyleConfig,
    isForPdf: Boolean = false  // ✅ PDF 모드 플래그
) {
    // ✅ PDF 모드일 때는 특정 시간으로 고정 (예: 15초)
    var timerSeconds by remember {
        mutableStateOf(if (isForPdf) 15 else 0)
    }

    // ✅ PDF 모드가 아닐 때만 타이머 작동
    if (!isForPdf) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                timerSeconds++
            }
        }
    }

    // ✅ PDF 모드에서는 고정된 높이 값 사용
    val waves = if (isForPdf) {
        // PDF용 고정 높이 (다양한 높이로 웨이브 느낌)
        listOf(0.6f, 0.9f, 0.7f, 0.5f, 0.8f)
    } else {
        // 일반 모드: 애니메이션
        val infiniteTransition = rememberInfiniteTransition(label = "wave")
        (0..4).map { index ->
            infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500 + (index * 100), easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "wave$index"
            ).value
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Recording...",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Timer Text
        Text(
            text = String.format("00:%02d", timerSeconds),
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Waveform Visualizer
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(100.dp)
        ) {
            waves.forEach { height ->
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .fillMaxHeight(height)  // ✅ PDF에서는 고정값, 앱에서는 애니메이션값
                        .background(style.primaryColor, RoundedCornerShape(50))
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Record Button (Stop)
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(4.dp, style.primaryColor.copy(alpha = 0.3f), CircleShape)
                .padding(8.dp)
                .background(Color.Red, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
        }
    }
}