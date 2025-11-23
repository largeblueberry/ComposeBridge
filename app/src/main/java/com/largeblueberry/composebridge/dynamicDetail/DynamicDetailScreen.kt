package com.largeblueberry.composebridge.dynamicDetail

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- 1. Data Models ---

data class UiStyleConfig(
    val targetName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val backgroundColor: Color,
    val cornerRadius: Dp,
    val fontFamilyName: String,
    val buttonShape: Shape
) {
    // Simple JSON serializer for MVP (No external libraries needed)
    fun toJsonString(): String {
        return """
        {
            "target": "$targetName",
            "style": {
                "primaryColor": "#${Integer.toHexString(primaryColor.toArgb())}",
                "secondaryColor": "#${Integer.toHexString(secondaryColor.toArgb())}",
                "backgroundColor": "#${Integer.toHexString(backgroundColor.toArgb())}",
                "cornerRadius": ${cornerRadius.value},
                "font": "$fontFamilyName"
            }
        }
        """.trimIndent()
    }
}

// --- 2. Mock Data ---

val styleDatabase = mapOf(
    "10대 학생" to UiStyleConfig(
        targetName = "10대 학생",
        primaryColor = Color(0xFFFFEB3B),
        secondaryColor = Color(0xFF000000),
        backgroundColor = Color(0xFF212121),
        cornerRadius = 24.dp,
        fontFamilyName = "Cursive",
        buttonShape = RoundedCornerShape(24.dp)
    ),
    "20대 여성" to UiStyleConfig(
        targetName = "20대 여성",
        primaryColor = Color(0xFFFFC0CB),
        secondaryColor = Color(0xFF8E2DE2),
        backgroundColor = Color(0xFFFFF0F5),
        cornerRadius = 16.dp,
        fontFamilyName = "Serif",
        buttonShape = RoundedCornerShape(16.dp)
    ),
    "30대 직장인" to UiStyleConfig(
        targetName = "30대 직장인",
        primaryColor = Color(0xFF1A237E),
        secondaryColor = Color(0xFFFFFFFF),
        backgroundColor = Color(0xFFE8EAF6),
        cornerRadius = 4.dp,
        fontFamilyName = "SansSerif",
        buttonShape = RoundedCornerShape(4.dp)
    ),
    "실버 세대" to UiStyleConfig(
        targetName = "실버 세대",
        primaryColor = Color(0xFF4CAF50),
        secondaryColor = Color(0xFFFFFFFF),
        backgroundColor = Color(0xFFF1F8E9),
        cornerRadius = 8.dp,
        fontFamilyName = "Monospace",
        buttonShape = RoundedCornerShape(8.dp)
    )
)

val targetList = styleDatabase.keys.toList()

// --- 3. Main Screen ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicDetailScreen(
    screenType: String = "chat" // Default to chat for demo
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val pagerState = rememberPagerState(pageCount = { targetList.size })
    val coroutineScope = rememberCoroutineScope()

    // State for Stamp Animation
    var showStamp by remember { mutableStateOf(false) }

    val currentTarget = targetList[pagerState.currentPage]
    val currentStyle = styleDatabase[currentTarget] ?: styleDatabase.values.first()

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
                    // Inject Style into Template
                    // Key is important to reset animation when style changes
                    key(currentStyle.targetName, screenType) {
                        when (screenType) {
                            "login" -> LoginTemplate(currentStyle)
                            "chat" -> ChatTemplate(currentStyle)
                            else -> GenericTemplate(screenType, currentStyle)
                        }
                    }

                    // --- Stamp Overlay (Inside Phone Frame) ---
                    if (showStamp) {
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
                            text = targetList[page],
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
                        coroutineScope.launch {
                            // 1. Trigger Stamp Animation
                            showStamp = true
                            // 2. Haptic Feedback (Vibration)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                            // 3. Wait for animation
                            delay(1500)

                            // 4. Share Intent
                            shareJsonStyle(context, currentStyle)

                            // 5. Reset
                            showStamp = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CONFIRM & SHARE JSON", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- 4. Templates with Fake Animations ---

@Composable
fun ChatTemplate(style: UiStyleConfig) {
    // Fake Data for Animation
    val messages = remember {
        listOf(
            "안녕하세요! 디자인 어때요?" to false, // Received
            "오! ${style.targetName} 취향저격인데요?" to true, // Sent
            "이대로 개발 진행시킬까요?" to false,
            "네! JSON 보내주세요!" to true
        )
    }

    // Animation State: How many messages to show
    var visibleCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Simulate messages arriving one by one
        messages.indices.forEach { i ->
            delay(600) // Delay between messages
            visibleCount = i + 1
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(style.primaryColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "  Chat (${style.targetName})",
                color = style.secondaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages.take(visibleCount)) { (text, isMe) ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
                ) {
                    if (isMe) {
                        // Sent Message (Dynamic Style)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        style.primaryColor,
                                        RoundedCornerShape(
                                            topStart = style.cornerRadius,
                                            bottomStart = style.cornerRadius,
                                            bottomEnd = style.cornerRadius
                                        )
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(text, color = style.secondaryColor, fontSize = 14.sp)
                            }
                        }
                    } else {
                        // Received Message (Static Style)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Box(modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Gray))
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(
                                            topEnd = 16.dp,
                                            bottomEnd = 16.dp,
                                            bottomStart = 16.dp
                                        )
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(text, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginTemplate(style: UiStyleConfig) {
    // Simple Breathing Animation for Logo
    val infiniteTransition = rememberInfiniteTransition(label = "logo")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = style.primaryColor,
            modifier = Modifier
                .size(64.dp)
                .scale(scale) // Apply Animation
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White, style.buttonShape)
                .border(1.dp, style.primaryColor.copy(alpha = 0.5f), style.buttonShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Email", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(style.primaryColor, style.buttonShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "LOGIN",
                color = style.secondaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun GenericTemplate(name: String, style: UiStyleConfig) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = style.primaryColor)
    }
}

// --- 5. Stamp Animation Component ---

@Composable
fun StampOverlay(color: Color, textColor: Color) {
    // Zoom In + Fade In Animation
    val scale = remember { Animatable(2.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) }
        launch { alpha.animateTo(1f, animationSpec = tween(100)) }
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

// --- 6. Share Logic (Intent) ---

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