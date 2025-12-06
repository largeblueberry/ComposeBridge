package com.largeblueberry.composebridge.timemachine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.graphics.scale
import com.largeblueberry.composebridge.domain.ComposableToBitmapConverter
import com.largeblueberry.composebridge.domain.TemplateMapper
import com.largeblueberry.data.cart.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneratePdfUseCase @Inject constructor(
    private val bitmapConverter: ComposableToBitmapConverter,
    private val pdfFileHandler: PdfFileHandler
) {

    suspend fun executeAndShare(
        context: Context,
        capsule: TimeCapsule
    ): Result<File> = withContext(Dispatchers.IO) {
        runCatching {
            val pdfDocument = PdfDocument()

            try {
                capsule.items.forEachIndexed { index, cartItem ->
                    val pageNumber = index * 2

                    val uiBitmap = captureTemplateBitmap(context, cartItem)
                    println("🔍 [PDF] 캡처 완료 - bitmap size: ${uiBitmap.width} x ${uiBitmap.height}")

                    addImagePage(pdfDocument, uiBitmap, pageNumber)
                    uiBitmap.recycle()

                    val jsonData = cartItem.styleConfig.toJsonString()
                    addTextPage(pdfDocument, jsonData, cartItem.templateName, pageNumber + 1)
                }

                val fileName = generateFileName(capsule)
                val file = pdfFileHandler.savePdfToFile(pdfDocument, fileName)
                pdfFileHandler.sharePdfFile(file)

                file
            } finally {
                pdfDocument.close()
            }
        }
    }

    private suspend fun captureTemplateBitmap(
        context: Context,
        cartItem: CartItem
    ): Bitmap {
        // ✅ dp → px 변환
        val density = context.resources.displayMetrics.density
        val widthPx = (595 * density).toInt()
        val heightPx = (842 * density).toInt()

        println("🔍 [PDF] density: $density")
        println("🔍 [PDF] 캡처 시작 - ${widthPx}px × ${heightPx}px (595dp × 842dp)")

        val bitmap = bitmapConverter.captureToBitmap(
            context = context,
            width = widthPx,   // ✅ px 단위로 전달
            height = heightPx
        ) {
            TemplateMapper.GetTemplate(
                templateName = cartItem.templateName,
                styleConfig = cartItem.styleConfig,
                isForPdf = true
            )
        }

        println("🔍 [PDF] 원본 캡처 완료 - ${bitmap.width} × ${bitmap.height}")

        // ✅ PDF 크기(595×842)로 스케일링
        val scaledBitmap = bitmap.scale(595, 842)

        // ✅ 원본 비트맵 메모리 해제
        if (scaledBitmap != bitmap) {
            bitmap.recycle()
        }

        return scaledBitmap
    }

    private fun addImagePage(pdfDocument: PdfDocument, bitmap: Bitmap, pageNumber: Int) {
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // ✅ 이미 스케일링된 비트맵이므로 그대로 사용
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        pdfDocument.finishPage(page)
    }

    private fun addTextPage(
        pdfDocument: PdfDocument,
        jsonData: String,
        templateName: String,
        pageNumber: Int
    ) {
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 10f
            typeface = Typeface.MONOSPACE
        }

        var yPosition = 50f
        canvas.drawText("Template: $templateName", 30f, yPosition, paint)
        yPosition += 30f

        jsonData.lines().forEach { line ->
            if (yPosition > pageHeight - 50) return@forEach
            canvas.drawText(line, 30f, yPosition, paint)
            yPosition += 15f
        }

        pdfDocument.finishPage(page)
    }

    private fun generateFileName(capsule: TimeCapsule): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        return "TimeCapsule_${capsule.name}_$timestamp.pdf"
    }
}