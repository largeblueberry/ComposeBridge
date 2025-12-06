package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

@Composable
fun BoardTemplate(
    style: UiStyleConfig,
    isForPdf: Boolean = false  // ✅ PDF 모드 플래그
) {
    // 가짜 게시글 데이터
    val posts = remember {
        listOf(
            "공지사항: 과제 제출 기한 안내",
            "자유게시판: 오늘 점심 뭐 먹지?",
            "질문: 컴포즈 애니메이션 너무 어려워요",
            "정보: 안드로이드 15 새로운 기능",
            "장터: 전공책 팝니다 (A급)",
            "동아리: 신입 부원 모집합니다!"
        )
    }

    // ✅ PDF 모드일 때는 모든 게시글 표시
    var visibleItemCount by remember {
        mutableStateOf(if (isForPdf) posts.size else 0)
    }

    // ✅ PDF 모드가 아닐 때만 순차 애니메이션
    if (!isForPdf) {
        LaunchedEffect(Unit) {
            posts.indices.forEach { _ ->
                delay(150) // 0.15초마다 하나씩 등장
                visibleItemCount++
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Community",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(posts.take(visibleItemCount)) { index, title ->
                    // ✅ PDF 모드에서는 AnimatedVisibility 없이 직접 표시
                    if (isForPdf) {
                        // PDF용: 애니메이션 없이 바로 표시
                        BoardPostItem(title = title, style = style)
                    } else {
                        // 앱용: 애니메이션 포함
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
                        ) {
                            BoardPostItem(title = title, style = style)
                        }
                    }
                }
            }
        }

        // ✅ Floating Action Button (PDF 모드에서는 고정, 앱에서는 펄스 애니메이션)
        val fabScale = if (isForPdf) {
            1f  // PDF용: 고정 크기
        } else {
            val infiniteTransition = rememberInfiniteTransition(label = "fab")
            infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                label = "fabScale"
            ).value
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(56.dp)
                .scale(fabScale)  // ✅ PDF에서는 1f, 앱에서는 애니메이션
                .background(style.primaryColor, CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = style.secondaryColor
            )
        }
    }
}

// ✅ 게시글 아이템을 별도 Composable로 분리
@Composable
private fun BoardPostItem(
    title: String,
    style: UiStyleConfig
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                Color.White,
                RoundedCornerShape(style.cornerRadius)
            )
            .border(
                1.dp,
                Color.Gray.copy(alpha = 0.2f),
                RoundedCornerShape(style.cornerRadius)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    style.primaryColor.copy(alpha = 0.2f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.first().toString(),
                fontWeight = FontWeight.Bold,
                color = style.primaryColor
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        // Text Content
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "익명 • 방금 전",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}