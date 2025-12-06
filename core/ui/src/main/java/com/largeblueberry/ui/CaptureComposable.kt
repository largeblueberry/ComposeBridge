package com.largeblueberry.ui

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Composable을 Bitmap으로 캡처하는 Modifier
 */
fun Modifier.capturable(onCaptured: (Bitmap) -> Unit): Modifier {
    var size by mutableStateOf(IntSize.Zero)
    var shouldCapture by mutableStateOf(false)

    return this
        .onSizeChanged { size = it }
        .drawWithCache {
            val width = size.width
            val height = size.height

            onDrawWithContent {
                drawContent()

                if (shouldCapture && width > 0 && height > 0) {
                    val bitmap = createBitmap(width, height)
                    val canvas = android.graphics.Canvas(bitmap)

                    drawIntoCanvas {
                        it.nativeCanvas.let { nativeCanvas ->
                            // 현재 그려진 내용을 bitmap에 복사
                            canvas.translate(-drawContext.canvas.nativeCanvas.clipBounds.left.toFloat(),
                                -drawContext.canvas.nativeCanvas.clipBounds.top.toFloat())
                        }
                    }

                    onCaptured(bitmap)
                    shouldCapture = false
                }
            }
        }
}

/**
 * 특정 Composable을 ImageBitmap으로 캡처
 * @param content 캡처할 Composable
 * @param onCapture 캡처된 ImageBitmap을 반환하는 콜백
 */
@Composable
fun CaptureComposable(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onCapture: (ImageBitmap) -> Unit
) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    androidx.compose.foundation.layout.Box(
        modifier = modifier.capturable { bitmap ->
            capturedBitmap = bitmap
        }
    ) {
        content()
    }

    LaunchedEffect(capturedBitmap) {
        capturedBitmap?.let {
            onCapture(it.asImageBitmap())
        }
    }
}