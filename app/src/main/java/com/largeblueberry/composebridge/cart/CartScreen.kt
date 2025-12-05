package com.largeblueberry.composebridge.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.largeblueberry.data.cart.CartItem
import com.largeblueberry.ui.BackgroundGray
import com.largeblueberry.ui.PrimaryBlue
import com.largeblueberry.ui.StampOverlay
import kotlinx.coroutines.delay

@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()

    if (cartItems.isEmpty()) {
        EmptyCartScreen(
            onBackClick = onBackClick,
            onGoToMarketClick = onBackClick, // 임시로 뒤로가기와 동일
            onGoToHomeClick = onBackClick
        )
    } else {
        CartWithItemsScreen(
            cartItems = cartItems,
            onBackClick = onBackClick,
            // 최종 승인 시 ViewModel의 함수를 호출하도록 연결합니다.
            onFinalApprove = { title ->
                // TODO: 여기에 PDF 추출 및 JSON 생성, 저장소 등록 등의 최종 로직을 구현합니다.
                // viewModel.finalApprove(title)
                println("Final Project Approved with title: $title")
            },
            onRemoveItem = { itemId -> viewModel.removeItem(itemId) }
        )
    }
}

@Composable
fun CartTopBar(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
            Text(
                text = "장바구니",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* 더보기 메뉴 */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기")
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
    }
}

@Composable
fun CartHeader(itemCount: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "총 ${itemCount}개 UI 템플릿",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ComposeMarket",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CartBottomBar(
    itemCount: Int,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFE0E0E0))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "선택된 UI 템플릿",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${itemCount}개",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }

                Button(
                    onClick = onCheckoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .width(120.dp)
                ) {
                    Text(
                        "최종 확정",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CartWithItemsScreen(
    cartItems: List<CartItem>,
    onBackClick: () -> Unit,
    // 수정된 부분: 매개변수 정의를 람다 타입으로 명시하고 기본값을 제거했습니다.
    onFinalApprove: (String) -> Unit,
    onRemoveItem: (Int) -> Unit
) {
    // 1. 상태 정의
    var showStamp by remember { mutableStateOf(false) }
    var showTitleDialog by remember { mutableStateOf(false) }

    // 2. 애니메이션 -> 다이얼로그 전환 로직
    if (showStamp) {
        LaunchedEffect(Unit) {
            // 도장 애니메이션을 1.5초간 보여준 후
            delay(1500)
            showStamp = false
            // 제목 입력 다이얼로그를 띄웁니다.
            showTitleDialog = true
        }
    }

    Scaffold(
        topBar = { CartTopBar(onBackClick) },
        // "최종 확정" 버튼 클릭 시 showStamp를 true로 설정하여 애니메이션 시작
        bottomBar = { CartBottomBar(cartItems.size, onCheckoutClick = { showStamp = true }) },
        containerColor = BackgroundGray
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 장바구니 헤더
            item {
                CartHeader(cartItems.size)
            }

            // 장바구니 아이템들
            items(cartItems) { item ->
                CartItemCard(
                    item = item,
                    onRemoveItem = { onRemoveItem(item.id) }
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    // 3. Stamp Overlay (도장 애니메이션)
    if (showStamp) {
        // 제공해주신 StampOverlay 컴포넌트 재사용
        StampOverlay(color = PrimaryBlue, textColor = Color.White)
    }

    // 4. Title Input Dialog (제목 입력)
    if (showTitleDialog) {
        ProjectTitleDialog(
            onDismiss = { showTitleDialog = false },
            onConfirm = { title ->
                showTitleDialog = false
                onFinalApprove(title) // 최종 확정 로직 실행 (상위 컴포저블로 전달)
            }
        )
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // 스타일 미리보기 (색상 팔레트)
                Column(
                    modifier = Modifier.padding(end = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 색상 미리보기
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(item.styleConfig.primaryColor)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(item.styleConfig.secondaryColor)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(item.styleConfig.backgroundColor)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.screenType.uppercase(),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                // 상품 정보
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.getDisplayName(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "타겟: ${item.styleConfig.targetName}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 무료 표시
                    Text(
                        text = "무료",
                        color = PrimaryBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 삭제 버튼
                IconButton(onClick = onRemoveItem) {
                    Icon(Icons.Default.Close, contentDescription = "삭제", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ProjectTitleDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("프로젝트 제목 입력", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("확정된 UI 템플릿으로 최종 프로젝트를 생성합니다.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("프로젝트 제목") },
                    placeholder = { Text("예: 캡스톤 디자인 기획서 v1.0") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title) },
                enabled = title.isNotBlank(), // 제목이 비어있지 않을 때만 활성화
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("확정 및 생성")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Color.Gray)
            }
        }
    )
}

// EmptyCartScreen 및 CartViewModel, CartItem 등은 생략
// Preview는 기존 코드를 유지합니다.
@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreen()
}