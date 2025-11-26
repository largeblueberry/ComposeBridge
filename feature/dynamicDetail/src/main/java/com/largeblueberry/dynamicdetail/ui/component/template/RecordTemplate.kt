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
import androidx.compose.runtime.mutableIntStateOf
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
fun RecordTemplate(style: UiStyleConfig) {
    var timerSeconds by remember { mutableIntStateOf(0) }

    // 타이머 작동
    LaunchedEffect(Unit) {
        while(true) {
            delay(1000)
            timerSeconds++
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "wave")

    // 5개의 막대가 서로 다르게 움직이도록 설정
    val waves = (0..4).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500 + (index * 100), easing = FastOutSlowInEasing), // 속도 다르게
                repeatMode = RepeatMode.Reverse
            ),
            label = "wave$index"
        )
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
            waves.forEach { animFloat ->
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .fillMaxHeight(animFloat.value) // 높이가 애니메이션됨
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