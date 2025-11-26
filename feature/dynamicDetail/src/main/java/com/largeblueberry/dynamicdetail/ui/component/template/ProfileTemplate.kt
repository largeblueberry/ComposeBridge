package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

@Composable
fun ProfileTemplate(style: UiStyleConfig) {
    // 1. Animation State
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    // 설정 항목들의 표시 상태
    var showSettings by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    // 프로필 이미지 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "profile")
    val profileScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ), label = "profileScale"
    )

    // 토글 색상 애니메이션
    val notificationColor by animateColorAsState(
        targetValue = if (notificationEnabled) style.primaryColor else Color.Gray,
        animationSpec = tween(300),
        label = "notificationColor"
    )

    val darkModeColor by animateColorAsState(
        targetValue = if (darkModeEnabled) style.primaryColor else Color.Gray,
        animationSpec = tween(300),
        label = "darkModeColor"
    )

    // 2. Fake Scenario Script
    LaunchedEffect(Unit) {
        delay(300)

        // 사용자 이름 타이핑
        val targetName = "홍길동"
        targetName.forEach { char ->
            userName += char
            delay(80)
        }
        delay(200)

        // 이메일 타이핑
        val targetEmail = "student@univ.ac.kr"
        targetEmail.forEach { char ->
            userEmail += char
            delay(40)
        }
        delay(500)

        // 설정 항목들 표시
        showSettings = true
        delay(800)

        // 알림 토글 ON
        notificationEnabled = true
        delay(1000)

        // 다크모드 토글 ON
        darkModeEnabled = true
        delay(1500)

        // 다크모드 다시 OFF (데모용)
        darkModeEnabled = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Profile Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(profileScale)
                .clip(CircleShape)
                .background(style.primaryColor.copy(alpha = 0.1f))
                .border(3.dp, style.primaryColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = style.primaryColor,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = if (userName.isEmpty()) "Loading..." else userName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (userName.isEmpty()) Color.Gray else Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // User Email
        Text(
            text = if (userEmail.isEmpty()) "Loading..." else userEmail,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Settings Section
        if (showSettings) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Settings Header
                Text(
                    text = "Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Notification Setting
                SettingItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    isEnabled = notificationEnabled,
                    color = notificationColor,
                    style = style
                )

                // Dark Mode Setting
                SettingItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    isEnabled = darkModeEnabled,
                    color = darkModeColor,
                    style = style
                )

                // Privacy Setting (Static)
                SettingItem(
                    icon = Icons.Default.Security,
                    title = "Privacy",
                    isEnabled = false,
                    color = Color.Gray,
                    style = style
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Red.copy(alpha = 0.1f), style.buttonShape)
                        .border(1.dp, Color.Red.copy(alpha = 0.3f), style.buttonShape),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Logout",
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    isEnabled: Boolean,
    color: Color,
    style: UiStyleConfig
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, style.buttonShape)
            .border(1.dp, color.copy(alpha = 0.3f), style.buttonShape)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        // Toggle Switch (시각적 표현)
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(24.dp)
                .background(
                    color = if (isEnabled) color.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = if (isEnabled) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = if (isEnabled) color else Color.Gray,
                        shape = CircleShape
                    )
                    .padding(2.dp)
            )
        }
    }
}