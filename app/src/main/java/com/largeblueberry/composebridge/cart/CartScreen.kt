package com.largeblueberry.composebridge.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.composebridge.ui.ContainerBadge
import com.largeblueberry.ui.BackgroundGray
import com.largeblueberry.ui.HotPink
import com.largeblueberry.ui.PrimaryBlue

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    val originalPrice: Int? = null,
    val quantity: Int,
    val isFree: Boolean = false,
    val badges: List<String> = emptyList()
)

@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(1, "심플 로그인 UI", 0, null, 1, true, listOf("무료")),
                CartItem(2, "[Compose] 만능 게시판 템플릿", 30000, 45000, 1, false, listOf("BEST", "할인")),
                CartItem(3, "채팅 UI 컴포넌트", 25000, null, 2, false, listOf("인기")),
                CartItem(4, "퀴즈 앱 템플릿", 15000, 20000, 1, false, listOf("NEW"))
            )
        )
    }

    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    val totalOriginalPrice = cartItems.sumOf { (it.originalPrice ?: it.price) * it.quantity }
    val discountAmount = totalOriginalPrice - totalPrice

    Scaffold(
        topBar = { CartTopBar(onBackClick) },
        bottomBar = { CartBottomBar(totalPrice, onCheckoutClick) },
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
                    onQuantityChange = { newQuantity ->
                        cartItems = cartItems.map {
                            if (it.id == item.id) it.copy(quantity = newQuantity) else it
                        }
                    },
                    onRemoveItem = {
                        cartItems = cartItems.filter { it.id != item.id }
                    }
                )
            }

            // 주문 요약
            item {
                OrderSummaryCard(
                    totalOriginalPrice = totalOriginalPrice,
                    discountAmount = discountAmount,
                    totalPrice = totalPrice
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
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
                text = "총 ${itemCount}개 상품",
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

@SuppressLint("DefaultLocale")
@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
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
                // 상품 이미지
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 상품 정보
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 배지들
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        item.badges.forEach { badge ->
                            val badgeColor = when (badge) {
                                "BEST" -> Color.Red
                                "무료" -> HotPink
                                "할인" -> Color(0xFFFF9800)
                                "NEW" -> PrimaryBlue
                                else -> Color.Gray
                            }
                            ContainerBadge(badge, badgeColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 가격
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (item.isFree) {
                            Text(
                                text = "무료",
                                color = HotPink,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            if (item.originalPrice != null && item.originalPrice != item.price) {
                                Text(
                                    text = "${String.format("%,d", item.originalPrice)}원",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = "${String.format("%,d", item.price)}원",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.originalPrice != null && item.originalPrice != item.price) Color.Red else Color.Black
                            )
                        }
                    }
                }

                // 삭제 버튼
                IconButton(onClick = onRemoveItem) {
                    Icon(Icons.Default.Close, contentDescription = "삭제", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 수량 조절
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("수량", fontSize = 14.sp, color = Color.Gray)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "수량 감소",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0))
                                .padding(2.dp)
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "수량 증가",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue)
                                .padding(2.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun OrderSummaryCard(
    totalOriginalPrice: Int,
    discountAmount: Int,
    totalPrice: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "주문 요약",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("상품 금액", color = Color.Gray)
                Text("${String.format("%,d", totalOriginalPrice)}원")
            }

            if (discountAmount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("할인 금액", color = Color.Gray)
                    Text("-${String.format("%,d", discountAmount)}원", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("배송비", color = Color.Gray)
                Text("무료", color = HotPink, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "총 결제 금액",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${String.format("%,d", totalPrice)}원",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CartBottomBar(
    totalPrice: Int,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                        "총 결제 금액",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${String.format("%,d", totalPrice)}원",
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
                        "주문하기",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreen()
}