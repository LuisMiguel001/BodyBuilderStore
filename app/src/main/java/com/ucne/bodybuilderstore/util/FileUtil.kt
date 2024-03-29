package com.ucne.bodybuilderstore.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File

object FileUtil {
    fun saveImage(context: Context, uri: Uri): String? {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imagenes")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imageName = "imagen_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(directory, imageName)

        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return destinationFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

