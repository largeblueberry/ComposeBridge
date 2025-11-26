package com.largeblueberry.composebridge.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.ui.BackgroundGray
import com.largeblueberry.ui.HotPink
import com.largeblueberry.ui.PrimaryBlue

@Composable
fun MainScreen(
    onNavigateToMarket: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar(onNavigateToCart = onNavigateToCart) },
        bottomBar = { SimpleBottomNavigation() },
        containerColor = BackgroundGray
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Banner Section
            item {
                PromoBanner(onNavigateToMarket)
            }

            // 2. Quick Menu (Screen Market Categories)
            item {
                CategorySection(onNavigateToMarket)
            }

            // 3. Horizontal Scroll (Hot Items)
            item {
                HotDealsSection()
            }

            // 4. Vertical Grid (Recommendations)
            item {
                Text(
                    text = "이런 UI는 어때요?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Note: LazyColumn inside LazyColumn is tricky, so we use items directly here
            // or a fixed height grid. For MVP, let's list items directly.
            items(10) { index ->
                ProductItemRow(index)
            }
        }
    }
}

// --- Components ---

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier, // 1. 외부에서 제어 가능하도록 파라미터 추가
    onNavigateToCart: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White) // 2. 배경색을 먼저 칠함 (상태바 영역까지 흰색으로)
            .statusBarsPadding()     // 3. 그 다음 패딩을 줘서 내용을 아래로 밀어냄
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ComposeMarket",
                color = PrimaryBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Alarm")
                Icon(
                    Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart",
                    modifier = Modifier.clickable { onNavigateToCart() }
                )
            }

        }

        // Search Bar Look-alike
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .border(1.dp, PrimaryBlue, RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F4FF), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("찾으시는 UI 디자인이 있나요?", color = Color.Gray)
            }
        }
    }
}


@Composable
fun PromoBanner(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(PrimaryBlue, Color(0xFF8E2DE2))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(24.dp)
        ) {
            ContainerBadge(text = "NEW OPEN", color = HotPink)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "나만의 앱 디자인\n여기서 쇼핑하세요",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("마켓 입장하기 >", color = PrimaryBlue, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategorySection(onNavigateToMarket: () -> Unit) {
    val categories = listOf(
        "로그인" to Icons.Default.Lock,
        "채팅" to Icons.Default.Email,
        "게시판" to Icons.AutoMirrored.Filled.List,
        "퀴즈" to Icons.Default.CheckCircle,
        "녹음" to Icons.Default.Call
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.forEach { (name, icon) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToMarket() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF4FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = name, tint = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun HotDealsSection() {
    Column(modifier = Modifier
        .background(Color.White)
        .padding(vertical = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔥 실시간 인기 UI", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("더보기", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                HotDealCard()
            }
        }
    }
}

@Composable
fun HotDealCard() {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Face, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text("심플 로그인 UI", fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("무료", color = HotPink, fontWeight = FontWeight.Bold)
                Text("⭐ 4.8 (120)", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProductItemRow(index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text("UI Kit $index", color = Color.Gray, fontSize = 10.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("[Compose] 만능 게시판 템플릿 $index", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ContainerBadge("BEST", Color.Red)
                Spacer(modifier = Modifier.width(4.dp))
                ContainerBadge("무료배송", Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("30,000원", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ContainerBadge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SimpleBottomNavigation() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("홈") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
            label = { Text("카테고리") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("마이페이지") }
        )
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(onNavigateToMarket = {}, onNavigateToCart = {})
}