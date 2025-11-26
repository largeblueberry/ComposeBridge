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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Share
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.largeblueberry.data.UiStyleConfig
import com.largeblueberry.dynamicdetail.ui.component.template.ChatTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.GenericTemplate
import com.largeblueberry.ui.StampOverlay
import com.largeblueberry.dynamicdetail.ui.component.template.BoardTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.FeedTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.LoginTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.ProfileTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.QuizTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.RecordTemplate
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicDetailScreen(
    screenType: String = "chat",
    viewModel: DynamicDetailViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 1. State Collecting
    val uiState by viewModel.uiState.collectAsState()

    // ViewModel에 스크린 타입 설정
    LaunchedEffect(screenType) {
        viewModel.setScreenType(screenType)
    }

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
                is DynamicDetailEffect.NavigateToCart -> {
                    // 스낵바로 카트 이동 제안
                    val result = snackbarHostState.showSnackbar(
                        message = "장바구니에서 확인하시겠습니까?",
                        actionLabel = "이동",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onNavigateToCart()
                    }
                }
                is DynamicDetailEffect.ShowAddedToCartMessage -> {
                    snackbarHostState.showSnackbar(
                        message = "장바구니에 추가되었습니다! 🛒",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    val currentStyle = uiState.currentStyle ?: return // 로딩 전이면 리턴하거나 로딩뷰 표시

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(paddingValues)
        ) {
            // Area 1: The Preview Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp) // 고정 높이로 설정
                    .background(Color.Black)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Phone Frame
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(540.dp) // 고정 높이로 설정
                        .clip(RoundedCornerShape(32.dp))
                        .background(currentStyle.backgroundColor)
                        .border(8.dp, Color.DarkGray, RoundedCornerShape(32.dp))
                ) {
                    key(currentStyle.targetName, screenType) {
                        when (screenType) {
                            "login" -> LoginTemplate(currentStyle)
                            "chat" -> ChatTemplate(currentStyle)
                            "quiz" -> QuizTemplate(currentStyle)
                            "board" -> BoardTemplate(currentStyle)
                            "record" -> RecordTemplate(currentStyle)
                            "profile" -> ProfileTemplate(currentStyle)
                            "feed" -> FeedTemplate(currentStyle)
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
                    .background(Color.White)
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("타겟 사용자를 선택하세요", fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                // Scroll Picker - 글자 정렬 수정
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // 선택된 항목을 표시하는 배경
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    )

                    VerticalPager(
                        state = pagerState,
                        pageSize = PageSize.Fixed(40.dp),
                        contentPadding = PaddingValues(vertical = 40.dp) // 위아래 패딩으로 중앙 정렬
                    ) { page ->
                        val isSelected = pagerState.currentPage == page
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp), // 페이지 크기와 동일하게
                            contentAlignment = Alignment.Center // 텍스트를 박스 중앙에 배치
                        ) {
                            Text(
                                text = uiState.targetList.getOrNull(page) ?: "",
                                fontSize = if (isSelected) 20.sp else 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else Color.LightGray,
                                modifier = Modifier.alpha(if (isSelected) 1f else 0.5f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 장바구니에 추가 버튼 (메인)
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.onConfirmClicked()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("장바구니에 추가", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // 공유 버튼 (서브)
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.onShareClicked()
                        },
                        modifier = Modifier
                            .height(56.dp)
                            .width(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "공유", tint = Color.Black)
                    }
                }

                // 하단 여백 추가 (스크롤 시 버튼이 완전히 보이도록)
                Spacer(modifier = Modifier.height(24.dp))
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