package com.largeblueberry.dynamicdetail.data

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.largeblueberry.data.UiStyleConfig

class StyleRepository {
    private val database = mapOf(
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

    fun getAllTargets(): List<String> = database.keys.toList()

    fun getStyle(target: String): UiStyleConfig {
        return database[target] ?: database.values.first()
    }
}