package com.example.multiki.presentation.utils

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

fun saveBitmapToFile(
    bitmap: Bitmap,
    application: Application,
    fileName: String
): Boolean {
    return try {
        application.openFileOutput("$fileName.jpg", MODE_PRIVATE).use { stream ->
            if(!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Couldn't save bitmap.")
            }
        }
        true
    } catch(e: IOException) {
        e.printStackTrace()
        false
    }
}

suspend fun getBitMap(
    fileName: String,
    application: Application
): Bitmap? {
    return withContext(Dispatchers.IO) {
        val file = File(application.filesDir, "$fileName.jpg")
        if(file.canRead() && file.isFile) {
            val bytes = file.readBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            null
        }
    }
}

fun deleteFile(application: Application, fileName: String): Boolean {
    return try {
        application.deleteFile("$fileName.jpg")
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
