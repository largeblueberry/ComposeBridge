package com.largeblueberry.composebridge.domain

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComposableToBitmapConverter @Inject constructor() {

    // ✅ Context를 파라미터로 받음
    suspend fun captureToBitmap(
        context: Context,  // ✅ 파라미터로 전달
        width: Int,
        height: Int,
        content: @Composable () -> Unit
    ): Bitmap = withContext(Dispatchers.Main) {

        val activity = context as? Activity
            ?: throw IllegalArgumentException("Context must be an Activity")

        val rootView = activity.window.decorView as ViewGroup

        // ✅ Recomposition 완료 신호를 받기 위한 플래그
        var isReady = false

        val composeView = ComposeView(context).apply {
            setContent {
                content()  // ✅ 실제 콘텐츠 렌더링

                // ✅ Recomposition이 완료되면 플래그 설정
                LaunchedEffect(Unit) {
                    delay(50)  // ✅ 한 프레임 대기
                    isReady = true
                }
            }
            layoutParams = ViewGroup.LayoutParams(width, height)
            visibility = View.INVISIBLE
        }


        rootView.addView(composeView)

        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        composeView.layout(0, 0, width, height)

        var waitTime = 0
        while (!isReady && waitTime < 2000) {
            delay(50)
            waitTime += 50
        }

        delay(100)

        val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        composeView.draw(canvas)

        rootView.removeView(composeView)

        bitmap
    }
}