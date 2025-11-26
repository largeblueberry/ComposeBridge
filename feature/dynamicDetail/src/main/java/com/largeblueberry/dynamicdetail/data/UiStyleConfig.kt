package com.largeblueberry.dynamicdetail.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp

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
    companion object {
        // MVP용 수동 파서 (Regex 활용)
        fun fromJson(json: String): UiStyleConfig {
            try {
                // 1. 값 추출을 위한 간단한 정규식
                fun extract(key: String): String {
                    val pattern = "\"$key\":\\s*\"?([^,\"}]+)\"?".toRegex()
                    return pattern.find(json)?.groupValues?.get(1) ?: ""
                }

                // 2. Color 파싱 (#AARRGGBB 형태 대응)
                fun parseColor(hex: String): Color {
                    return try {
                        // #이 없으면 붙여주고, Long으로 변환 후 Color 생성
                        val cleanHex = hex.replace("#", "")
                        Color(cleanHex.toLong(16))
                    } catch (e: Exception) {
                        Color.Black // Fallback
                    }
                }

                val primary = parseColor(extract("primaryColor"))
                val secondary = parseColor(extract("secondaryColor"))
                val background = parseColor(extract("backgroundColor"))
                val radiusVal = extract("cornerRadius").toFloatOrNull() ?: 0f
                val fontName = extract("font")
                val target = extract("target").ifEmpty { "Restored Config" }

                return UiStyleConfig(
                    targetName = target,
                    primaryColor = primary,
                    secondaryColor = secondary,
                    backgroundColor = background,
                    cornerRadius = Dp(radiusVal),
                    fontFamilyName = fontName,
                    buttonShape = androidx.compose.foundation.shape.RoundedCornerShape(Dp(radiusVal)) // Shape 복원
                )
            } catch (e: Exception) {
                // 파싱 실패 시 기본값 반환 (앱 죽는 것 방지)
                return UiStyleConfig(
                    "Error", Color.Gray, Color.Gray, Color.White, Dp(0f), "Default",
                    androidx.compose.foundation.shape.RoundedCornerShape(0)
                )
            }
        }
    }
}