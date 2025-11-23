package com.largeblueberry.composebridge.dynamicDetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. Data Models (The "JSON" Schema) ---

data class UiStyleConfig(
    val targetName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val backgroundColor: Color,
    val cornerRadius: Dp,
    val fontFamilyName: String, // For code generation string
    val buttonShape: Shape
)

// --- 2. Mock Data (Pre-defined JSON Styles) ---

val styleDatabase = mapOf(
    "10대 학생" to UiStyleConfig(
        targetName = "10대 학생",
        primaryColor = Color(0xFFFFEB3B), // Yellow
        secondaryColor = Color(0xFF000000),
        backgroundColor = Color(0xFF212121), // Dark Mode vibe
        cornerRadius = 24.dp,
        fontFamilyName = "Cursive",
        buttonShape = RoundedCornerShape(24.dp)
    ),
    "20대 여성" to UiStyleConfig(
        targetName = "20대 여성",
        primaryColor = Color(0xFFFFC0CB), // Pink
        secondaryColor = Color(0xFF8E2DE2),
        backgroundColor = Color(0xFFFFF0F5), // Lavender Blush
        cornerRadius = 16.dp,
        fontFamilyName = "Serif",
        buttonShape = RoundedCornerShape(16.dp)
    ),
    "30대 직장인" to UiStyleConfig(
        targetName = "30대 직장인",
        primaryColor = Color(0xFF1A237E), // Indigo
        secondaryColor = Color(0xFFFFFFFF),
        backgroundColor = Color(0xFFE8EAF6),
        cornerRadius = 4.dp,
        fontFamilyName = "SansSerif",
        buttonShape = RoundedCornerShape(4.dp)
    ),
    "실버 세대" to UiStyleConfig(
        targetName = "실버 세대",
        primaryColor = Color(0xFF4CAF50), // Green
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
    screenType: String // "login", "chat", etc.
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { targetList.size })
    val coroutineScope = rememberCoroutineScope()

    // [Step 4 Logic] Recomposition happens here based on scroll
    val currentTarget = targetList[pagerState.currentPage]
    val currentStyle = styleDatabase[currentTarget] ?: styleDatabase.values.first()

    Column(modifier = Modifier.fillMaxSize()) {

        // Area 1: The Preview Canvas (Dynamic UI)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black) // Canvas background
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Phone Frame Simulation
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(0.9f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(currentStyle.backgroundColor)
                    .border(8.dp, Color.DarkGray, RoundedCornerShape(32.dp))
            ) {
                // Inject Style into Template
                when (screenType) {
                    "login" -> LoginTemplate(currentStyle)
                    "chat" -> ChatTemplate(currentStyle)
                    else -> GenericTemplate(screenType, currentStyle)
                }
            }
        }

        // Area 2: Control Panel (Scroll Picker & Stamp)
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

            // [Step 4] Scroll Picker
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Selection Indicator
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

            // [Step 5] Stamp Button
            Button(
                onClick = {
                    generateAndCopyCode(context, screenType, currentStyle)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("STAMP & CODE COPY", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 4. Templates (The "Magic" Part) ---

@Composable
fun LoginTemplate(style: UiStyleConfig) {
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
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Dynamic Input Field
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

        // Dynamic Button
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
fun ChatTemplate(style: UiStyleConfig) {
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

        // Messages
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Received
            Row(verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.Gray))
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
                        .padding(12.dp)
                ) {
                    Text("안녕하세요! 디자인 어때요?", fontSize = 14.sp)
                }
            }

            // Sent (Dynamic Style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .background(style.primaryColor, RoundedCornerShape(topStart = style.cornerRadius, bottomStart = style.cornerRadius, bottomEnd = style.cornerRadius))
                        .padding(12.dp)
                ) {
                    Text("오! ${style.targetName} 취향저격인데요?", color = style.secondaryColor, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun GenericTemplate(name: String, style: UiStyleConfig) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = style.primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Style: ${style.targetName}", color = Color.Gray)
        }
    }
}

// --- 5. Code Generator Logic ---

fun generateAndCopyCode(context: Context, screenType: String, style: UiStyleConfig) {
    val code = """
        // [Generated by ScreenMarket]
        // Target: ${style.targetName}
        // Screen: $screenType
        
        @Composable
        fun ${screenType.capitalize()}Screen() {
            val primaryColor = Color(${style.primaryColor.value.toULong()})
            val shape = RoundedCornerShape(${style.cornerRadius.value}.dp)
            
            Column(modifier = Modifier.background(Color(${style.backgroundColor.value.toULong()}))) {
                // Your Dynamic UI Code Here
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = shape
                ) {
                    Text("Generated Button")
                }
            }
        }
    """.trimIndent()

    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Compose Code", code)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, "Compose 코드가 복사되었습니다!\nIDE에 붙여넣기 하세요.", Toast.LENGTH_LONG).show()
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}