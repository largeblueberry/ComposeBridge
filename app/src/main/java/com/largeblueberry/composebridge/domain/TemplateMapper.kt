package com.largeblueberry.composebridge.domain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.largeblueberry.data.UiStyleConfig
import com.largeblueberry.dynamicdetail.ui.component.template.BoardTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.FeedTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.LoginTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.ProfileTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.QuizTemplate
import com.largeblueberry.dynamicdetail.ui.component.template.RecordTemplate

/**
 * 템플릿 이름을 실제 Composable로 매핑하는 유틸리티
 */
object TemplateMapper {

    @Composable
    fun GetTemplate(templateName: String, styleConfig: UiStyleConfig) {
        when (templateName) {
            "LoginTemplate" -> LoginTemplate(styleConfig)
            "ProfileTemplate" -> ProfileTemplate(styleConfig)
            "FeedTemplate" -> FeedTemplate(styleConfig)
            "BoardTemplate" -> BoardTemplate(styleConfig)
            "QuizTemplate" -> QuizTemplate(styleConfig)
            "RecordTemplate" -> RecordTemplate(styleConfig)
            else -> DefaultTemplate(templateName, styleConfig)
        }
    }

    /**
     * 템플릿이 존재하지 않을 경우 기본 템플릿
     */
    @Composable
    private fun DefaultTemplate(name: String, styleConfig: UiStyleConfig) {
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .background(styleConfig.backgroundColor),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "$name\n(템플릿 준비 중)",
                color = styleConfig.primaryColor,
                fontSize = 24.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}