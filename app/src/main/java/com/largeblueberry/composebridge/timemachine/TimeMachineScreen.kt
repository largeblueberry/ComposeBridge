package com.largeblueberry.composebridge.timemachine

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.cart.CartItem
import com.largeblueberry.ui.BackgroundGray
import com.largeblueberry.ui.PrimaryBlue
import com.largeblueberry.ui.TimeTravelOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// 데이터 모델: 하나의 '타임캡슐'을 정의합니다. (Git의 Commit에 해당)
data class TimeCapsule(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val timestamp: Long = System.currentTimeMillis(),
    val items: List<CartItem> // 확정된 UI 템플릿 목록
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeMachineScreen(
    timeCapsules: List<TimeCapsule>,
    isLoading: Boolean = false,
    error: String? = null,
    onBackClick: () -> Unit,
    onCapsuleClick: (TimeCapsule) -> Unit,
    onRefresh: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isTimeTraveling by remember { mutableStateOf(false) }

    // 새 아이템이 추가될 때마다 리스트의 가장 위로 스크롤
    LaunchedEffect(timeCapsules.size) {
        if (timeCapsules.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("타임머신", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "새로고침")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error, color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRefresh) {
                            Text("다시 시도")
                        }
                    }
                }
                timeCapsules.isEmpty() -> {
                    Text(
                        "아직 생성된 타임캡슐이 없습니다.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                    ) {
                        items(timeCapsules, key = { it.id }) { capsule ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(durationMillis = 500)
                                ) + fadeIn(animationSpec = tween(durationMillis = 400)),
                                exit = fadeOut(animationSpec = tween(durationMillis = 300))
                            ) {
                                TimeCapsuleItem(
                                    capsule = capsule,
                                    onClick = {
                                        isTimeTraveling = true
                                        coroutineScope.launch {
                                            delay(1500)
                                            isTimeTraveling = false
                                            onCapsuleClick(capsule)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isTimeTraveling) {
                TimeTravelOverlay()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleItem(
    capsule: TimeCapsule,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // 타임라인 시각적 요소 (선과 점)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(100.dp) // 카드 높이에 맞춰 조절
                    .background(Color.LightGray)
            )
        }

        // 타임캡슐 정보 카드
        Card(
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = capsule.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTimestamp(capsule.timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${capsule.items.size}개의 UI 템플릿 포함",
                    color = PrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeMachineScreen() {
    // 미리보기를 위한 가짜 데이터
    val fakeCapsules = listOf(
        TimeCapsule(name = "초기 MVP 디자인 확정", items = emptyList()),
        TimeCapsule(name = "로그인 플로우 개선안", items = emptyList()),
    )
    TimeMachineScreen(
        timeCapsules = fakeCapsules,
        onBackClick = {},
        onCapsuleClick = {}
    )
}
