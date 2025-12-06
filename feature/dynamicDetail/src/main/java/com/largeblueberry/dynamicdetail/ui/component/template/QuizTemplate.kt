package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

@Composable
fun QuizTemplate(
    style: UiStyleConfig,
    isForPdf: Boolean = false  // ✅ PDF 모드 플래그
) {
    val options = listOf("Jetpack Compose", "XML Layouts", "Flutter", "React Native")
    val correctIndex = 0

    // ✅ PDF 모드일 때는 정답이 선택된 상태로 초기화
    var selectedIndex by remember {
        mutableIntStateOf(if (isForPdf) correctIndex else -1)
    }

    // ✅ PDF 모드가 아닐 때만 자동 선택 애니메이션
    if (!isForPdf) {
        LaunchedEffect(Unit) {
            delay(1500)  // 1.5초 후 자동으로 정답 선택
            selectedIndex = correctIndex
        }
    }

    //디버깅
    LaunchedEffect(selectedIndex) {
        println("🔍 QuizTemplate - isForPdf: $isForPdf, selectedIndex: $selectedIndex")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Question Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(style.primaryColor.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(
                "Android의 최신 UI 툴킷 이름은?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = style.primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Options
        options.forEachIndexed { index, text ->
            val isSelected = selectedIndex == index
            val isCorrect = index == correctIndex

            // ✅ 선택 시 색상 애니메이션 (PDF 모드에서는 즉시 적용)
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) {
                    if (isCorrect) style.primaryColor else Color.Red.copy(alpha = 0.7f)
                } else Color.White,
                animationSpec = if (isForPdf) snap() else tween(300),
                label = "bgColor"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) style.secondaryColor else Color.Black,
                animationSpec = if (isForPdf) snap() else tween(300),
                label = "textColor"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(56.dp)
                    .background(backgroundColor, style.buttonShape)
                    .border(1.dp, style.primaryColor.copy(alpha = 0.3f), style.buttonShape)
                    .clickable(enabled = !isForPdf) { selectedIndex = index },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    // 정답 체크 아이콘 표시
                    if (isSelected && isCorrect) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = textColor
                        )
                    }
                }
            }
        }
    }
}