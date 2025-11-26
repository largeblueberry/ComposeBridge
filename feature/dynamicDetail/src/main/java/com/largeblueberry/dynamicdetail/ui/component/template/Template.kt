package com.largeblueberry.dynamicdetail.ui.component.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import kotlinx.coroutines.delay

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
fun GenericTemplate(name: String, style: UiStyleConfig) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = style.primaryColor)
    }
}