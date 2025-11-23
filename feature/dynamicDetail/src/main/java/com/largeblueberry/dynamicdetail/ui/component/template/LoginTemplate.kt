package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.dynamicdetail.data.UiStyleConfig
import kotlinx.coroutines.delay

@Composable
fun LoginTemplate(style: UiStyleConfig) {
    // 1. Animation State
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    // 로그인 상태 관리 (0: 대기, 1: 로딩중, 2: 성공)
    var loginState by remember { mutableIntStateOf(0) }

    // 버튼 색상 애니메이션 (성공하면 초록색으로 변함)
    val buttonColor by animateColorAsState(
        targetValue = if (loginState == 2) Color(0xFF4CAF50) else style.primaryColor,
        animationSpec = tween(500),
        label = "buttonColor"
    )

    // 로고 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "logo")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    // 2. Fake Scenario Script
    LaunchedEffect(Unit) {
        delay(500)

        // 이메일 타이핑
        val targetEmail = "student@univ.ac.kr"
        targetEmail.forEach { char ->
            emailText += char
            delay(50) // 조금 더 빠르게
        }
        delay(200)

        // 비밀번호 타이핑
        val targetPassword = "password1234"
        targetPassword.forEach { _ ->
            passwordText += "•"
            delay(50)
        }
        delay(300)

        // [상태 1] 로딩 시작
        loginState = 1
        delay(1500) // 서버 통신하는 척 (1.5초)

        // [상태 2] 로그인 성공!
        loginState = 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = style.primaryColor,
            modifier = Modifier
                .size(64.dp)
                .scale(scale)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White, style.buttonShape)
                .border(1.dp, style.primaryColor.copy(alpha = 0.5f), style.buttonShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(if (emailText.isEmpty()) "Email" else emailText,
                color = if(emailText.isEmpty()) Color.Gray.copy(0.5f) else Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White, style.buttonShape)
                .border(1.dp, style.primaryColor.copy(alpha = 0.5f), style.buttonShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(if (passwordText.isEmpty()) "Password" else passwordText,
                color = if(passwordText.isEmpty()) Color.Gray.copy(0.5f) else Color.Black,
                fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button (핵심 애니메이션 부분)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(buttonColor, style.buttonShape), // 색상이 애니메이션됨
            contentAlignment = Alignment.Center
        ) {
            // 상태에 따라 내용물 교체 (Crossfade 느낌)
            androidx.compose.animation.AnimatedContent(
                targetState = loginState,
                label = "loginState"
            ) { state ->
                when (state) {
                    0 -> { // 기본 상태
                        Text(
                            "LOGIN",
                            color = style.secondaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    1 -> { // 로딩 중
                        CircularProgressIndicator(
                            color = style.secondaryColor,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    2 -> { // 성공!
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = style.secondaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Success!",
                                color = style.secondaryColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
