package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

data class FeedPost(
    val id: Int,
    val userName: String,
    val userHandle: String,
    val content: String,
    val timeAgo: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false
)

@Composable
fun FeedTemplate(style: UiStyleConfig) {
    // 1. Animation State
    var visiblePosts by remember { mutableIntStateOf(0) }
    var likedPosts by remember { mutableStateOf(setOf<Int>()) }

    // 포스트 데이터
    val posts = remember {
        listOf(
            FeedPost(1, "홍길동", "@kimcs", "오늘 점심 뭐 먹지? 제육 vs 돈까스 🤔", "2분 전", 12, 3),
            FeedPost(2, "이영희", "@leeyh", "드디어 금요일! 주말에 맛집 탐방 계획 세웠어요. 다들 주말 계획은? 🎉", "15분 전", 28, 7),
            FeedPost(3, "김철수", "@parkms", "새로운 카페 발견했는데 분위기 좋네요 ☕\n공부하기 딱 좋은 곳!", "1시간 전", 45, 12),
            FeedPost(4, "최지원", "@choijw", "반려견 '토리'가 새 장난감을 물고 왔어요. 너무 귀여워서 심장이 아프다 🐶", "3시간 전", 67, 23)
        )
    }

    // 2. Fake Scenario Script
    LaunchedEffect(Unit) {
        // 포스트들을 순차적으로 표시
        repeat(posts.size) { index ->
            delay(800)
            visiblePosts = index + 1
        }

        delay(1000)

        // 자동으로 몇 개 포스트에 좋아요
        delay(500)
        likedPosts = likedPosts + 1

        delay(1200)
        likedPosts = likedPosts + 3

        delay(800)
        likedPosts = likedPosts + 2
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(posts.take(visiblePosts)) { index, post ->
            FeedPostCard(
                post = post.copy(isLiked = post.id in likedPosts),
                style = style,
                onLikeClick = { postId ->
                    likedPosts = if (postId in likedPosts) {
                        likedPosts - postId
                    } else {
                        likedPosts + postId
                    }
                }
            )
        }
    }
}

@Composable
private fun FeedPostCard(
    post: FeedPost,
    style: UiStyleConfig,
    onLikeClick: (Int) -> Unit
) {
    // 좋아요 색상 애니메이션
    val likeColor by animateColorAsState(
        targetValue = if (post.isLiked) Color.Red else Color.Gray,
        animationSpec = tween(300),
        label = "likeColor"
    )

    // 좋아요 스케일 애니메이션
    var likePressed by remember { mutableStateOf(false) }
    val likeScale by animateFloatAsState(
        targetValue = if (likePressed) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "likeScale"
    )

    // 좋아요 애니메이션 트리거
    LaunchedEffect(post.isLiked) {
        if (post.isLiked) {
            likePressed = true
            delay(200)
            likePressed = false
        }
    }

    // 프로필 이미지 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "profile${post.id}")
    val profilePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + post.id * 200), // 각각 다른 속도
            repeatMode = RepeatMode.Reverse
        ), label = "profilePulse"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = style.buttonShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header (프로필 정보)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .scale(profilePulse)
                        .clip(CircleShape)
                        .background(style.primaryColor.copy(alpha = 0.1f))
                        .border(2.dp, style.primaryColor.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = style.primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${post.userHandle} • ${post.timeAgo}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // More options
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post Content
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = { onLikeClick(post.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = likeColor,
                            modifier = Modifier
                                .size(20.dp)
                                .scale(likeScale)
                        )
                    }
                    Text(
                        text = "${post.likes + if (post.isLiked) 1 else 0}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "${post.comments}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Share Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}