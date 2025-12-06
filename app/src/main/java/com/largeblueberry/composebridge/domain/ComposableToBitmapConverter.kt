package com.largeblueberry.composebridge.domain

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton  // ✅ Singleton으로 복구
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

        val composeView = ComposeView(context).apply {
            setContent { content() }
            layoutParams = ViewGroup.LayoutParams(width, height)
            visibility = View.INVISIBLE
        }

        rootView.addView(composeView)

        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        composeView.layout(0, 0, width, height)

        delay(100)

        val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        composeView.draw(canvas)

        rootView.removeView(composeView)

        bitmap
    }
}