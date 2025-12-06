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
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

@Composable
fun LoginTemplate(
    style: UiStyleConfig,
    isForPdf: Boolean = false  // ✅ PDF 모드 플래그
) {
    // ✅ PDF 모드일 때는 완료된 상태로 초기화
    var emailText by remember {
        mutableStateOf(if (isForPdf) "student@univ.ac.kr" else "")
    }
    var passwordText by remember {
        mutableStateOf(if (isForPdf) "••••••••••••" else "")
    }

    // 로그인 상태 관리 (0: 대기, 1: 로딩중, 2: 성공)
    var loginState by remember {
        mutableIntStateOf(if (isForPdf) 2 else 0)  // ✅ PDF는 성공 상태
    }

    // 버튼 색상 애니메이션 (PDF 모드에서는 즉시 적용)
    val buttonColor by animateColorAsState(
        targetValue = if (loginState == 2) Color(0xFF4CAF50) else style.primaryColor,
        animationSpec = if (isForPdf) snap() else tween(500),
        label = "buttonColor"
    )

    // 로고 애니메이션 (PDF 모드에서는 고정)
    val scale = if (isForPdf) {
        1f
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "logo")
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ), label = "scale"
        ).value
    }

    // ✅ PDF 모드가 아닐 때만 애니메이션 실행
    if (!isForPdf) {
        LaunchedEffect(Unit) {
            delay(500)

            // 이메일 타이핑
            val targetEmail = "student@univ.ac.kr"
            targetEmail.forEach { char ->
                emailText += char
                delay(50)
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
            delay(1500)

            // [상태 2] 로그인 성공!
            loginState = 2
        }
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
            Text(
                if (emailText.isEmpty()) "Email" else emailText,
                color = if (emailText.isEmpty()) Color.Gray.copy(0.5f) else Color.Black
            )
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
            Text(
                if (passwordText.isEmpty()) "Password" else passwordText,
                color = if (passwordText.isEmpty()) Color.Gray.copy(0.5f) else Color.Black,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(buttonColor, style.buttonShape),
            contentAlignment = Alignment.Center
        ) {
            // ✅ PDF 모드에서는 AnimatedContent 스킵하고 바로 최종 상태 표시
            if (isForPdf) {
                // 성공 상태 직접 표시
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
            } else {
                // 일반 모드에서는 애니메이션 적용
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
}