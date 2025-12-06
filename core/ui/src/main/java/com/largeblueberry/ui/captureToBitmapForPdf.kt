package com.largeblueberry.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import android.graphics.Canvas


/**
 * Composable을 Bitmap으로 변환 (PDF 생성용)
 * Activity Context가 필요합니다.
 */
// captureToBitmapForPdf.kt 수정
suspend fun captureToBitmapForPdf(
    context: Context,
    width: Int,
    height: Int,
    content: @Composable () -> Unit
): Bitmap = withContext(Dispatchers.Main) {

    // ComposeView는 메인 스레드에서 생성
    val composeView = ComposeView(context).apply {
        setContent { content() }
        layoutParams = ViewGroup.LayoutParams(width, height)
    }

    // 측정/레이아웃도 메인 스레드
    composeView.measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    )
    composeView.layout(0, 0, width, height)
    delay(100)

    // Bitmap 생성은 백그라운드에서
    val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)

    composeView.draw(canvas)

    bitmap
}