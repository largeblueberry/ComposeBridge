package com.largeblueberry.composebridge.timemachine

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfFileHandler @Inject constructor(
    @ApplicationContext private val context: Context // Application Context 주입
) {

    /**
     * PDF 문서를 파일로 저장하고, 저장된 파일 객체를 반환합니다.
     */
    fun savePdfToFile(pdfDocument: PdfDocument, fileName: String): File {
        val directory = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "PEGASUS"
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }

        return file
    }

    /**
     * PDF 파일을 공유 인텐트를 통해 외부에 공유합니다. (핵심 기능)
     */
    fun sharePdfFile(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // FileProvider 권한 문자열
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            // 공유 기능 강조를 위해 제목과 본문 추가
            putExtra(Intent.EXTRA_SUBJECT, "PEGASUS UI Specification - ${file.name}")
            putExtra(Intent.EXTRA_TEXT, "ComposeMarket에서 생성된 앱 화면 스펙 문서입니다.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "PDF 공유하기")
        // Application Context에서 Activity를 시작할 때 필요
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
