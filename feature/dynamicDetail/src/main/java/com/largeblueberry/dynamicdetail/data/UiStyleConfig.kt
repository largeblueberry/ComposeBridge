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
}
