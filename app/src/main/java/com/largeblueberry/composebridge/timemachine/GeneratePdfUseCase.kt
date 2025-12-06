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

    // ✅ Context를 파라미터로 받음
    suspend fun executeAndShare(
        context: Context,  // ✅ 추가
        capsule: TimeCapsule
    ): Result<File> = withContext(Dispatchers.IO) {
        runCatching {
            val pdfDocument = PdfDocument()

            try {
                capsule.items.forEachIndexed { index, cartItem ->
                    val pageNumber = index * 2

                    val uiBitmap = captureTemplateBitmap(context, cartItem)  // ✅ context 전달
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
        context: Context,  // ✅ 추가
        cartItem: CartItem
    ): Bitmap {
        println("🔍 [PDF] templateName: '${cartItem.templateName}'")
        println("🔍 [PDF] isForPdf: true")

        return bitmapConverter.captureToBitmap(
            context = context,  // ✅ 전달
            width = 595,
            height = 842
        ) {
            println("🔍 [PDF] Composable 렌더링 시작")
            TemplateMapper.GetTemplate(
                templateName = cartItem.templateName,
                styleConfig = cartItem.styleConfig,
                isForPdf = true
            )
        }
    }

    private fun addImagePage(pdfDocument: PdfDocument, bitmap: Bitmap, pageNumber: Int) {
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val scaledBitmap = bitmap.scale(pageWidth, pageHeight)
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)

        pdfDocument.finishPage(page)

        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
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