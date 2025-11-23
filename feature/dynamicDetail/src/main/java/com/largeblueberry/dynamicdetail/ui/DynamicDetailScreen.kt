package com.largeblueberry.dynamicdetail.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.largeblueberry.dynamicdetail.data.UiStyleConfig
import com.largeblueberry.dynamicdetail.ui.component.ChatTemplate
import com.largeblueberry.dynamicdetail.ui.component.GenericTemplate
import com.largeblueberry.dynamicdetail.ui.component.LoginTemplate
import com.largeblueberry.dynamicdetail.ui.component.StampOverlay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicDetailScreen(
    screenType: String = "chat",
    viewModel: DynamicDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // 1. State Collecting
    val uiState by viewModel.uiState.collectAsState()

    // Pager State는 UI 컨트롤러이므로 UI에 남겨두되, 변경사항을 VM에 알립니다.
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { uiState.targetList.size }
    )

    // 2. Pager 동기화: 사용자가 스크롤하면 ViewModel에 알림
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTargetSelected(pagerState.currentPage)
    }

    // 3. Side Effect Collecting: 공유 이벤트 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DynamicDetailEffect.ShareStyleJson -> {
                    shareJsonStyle(context, effect.style)
                }
            }
        }
    }

    val currentStyle = uiState.currentStyle ?: return // 로딩 전이면 리턴하거나 로딩뷰 표시

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Area 1: The Preview Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Phone Frame
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(0.9f)
                        .clip(RoundedCornerShape(32.dp))
                        .background(currentStyle.backgroundColor)
                        .border(8.dp, Color.DarkGray, RoundedCornerShape(32.dp))
                ) {
                    key(currentStyle.targetName, screenType) {
                        when (screenType) {
                            "login" -> LoginTemplate(currentStyle)
                            "chat" -> ChatTemplate(currentStyle)
                            else -> GenericTemplate(screenType, currentStyle)
                        }
                    }

                    // Stamp Overlay (State from ViewModel)
                    if (uiState.isStampVisible) {
                        StampOverlay(
                            color = currentStyle.primaryColor,
                            textColor = currentStyle.secondaryColor
                        )
                    }
                }
            }

            // Area 2: Control Panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color.White)
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("타겟 사용자를 선택하세요", fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                // Scroll Picker
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    )

                    VerticalPager(
                        state = pagerState,
                        pageSize = PageSize.Fixed(40.dp),
                        contentPadding = PaddingValues(vertical = 30.dp)
                    ) { page ->
                        val isSelected = pagerState.currentPage == page
                        Text(
                            text = uiState.targetList.getOrNull(page) ?: "",
                            fontSize = if (isSelected) 20.sp else 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            modifier = Modifier.alpha(if (isSelected) 1f else 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stamp & Share Button
                Button(
                    onClick = {
                        // UI 로직: 햅틱 피드백
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        // 비즈니스 로직: ViewModel 호출
                        viewModel.onConfirmClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CONFIRM & SHARE JSON", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun shareJsonStyle(context: Context, style: UiStyleConfig) {
    val jsonString = style.toJsonString()

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, jsonString)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "스타일 JSON 공유하기")
    context.startActivity(shareIntent)
}