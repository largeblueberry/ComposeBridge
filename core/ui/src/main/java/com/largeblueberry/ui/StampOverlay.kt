package com.largeblueberry.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StampOverlay(color: Color, textColor: Color) {
    // Zoom In + Fade In Animation
    val scale = remember { Animatable(2.5f) }
    val alpha = remember { Animatable(0f) }

    // 진동을 위한 HapticFeedback
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        // 첫 번째 강한 진동 (스탬프가 찍힐 때)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        launch {
            scale.animateTo(1f, animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ))
        }
        launch { alpha.animateTo(1f, animationSpec = tween(100)) }

        // 애니메이션 중간에 추가 진동들 (뽕맛을 위해)
        delay(150) // 첫 번째 바운스 후
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

        delay(100) // 두 번째 바운스 후
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

        delay(80) // 세 번째 작은 진동
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)), // Dim background
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
                .rotate(-15f) // Tilt for stamp effect
                .border(4.dp, color, RoundedCornerShape(16.dp))
                .padding(16.dp)
                .background(color.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "APPROVED",
                    color = textColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
            }
        }
    }
}