package com.largeblueberry.data

import androidx.room.TypeConverter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class Converters {

    @TypeConverter
    fun fromUiStyleConfig(config: UiStyleConfig): String {
        return config.toJsonString()
    }

    @TypeConverter
    fun toUiStyleConfig(json: String): UiStyleConfig {
        return UiStyleConfig.fromJson(json)
    }

    @TypeConverter
    fun fromColor(color: Color): Int {
        return color.toArgb()
    }

    @TypeConverter
    fun toColor(argb: Int): Color {
        return Color(argb)
    }
}