package com.hfad.news.tsivileva.newschannel.users_classes

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.FileOutputStream
import java.io.OutputStream

class ImageGallery {
    companion object {

        val fileName = "temp.png"
        val path= MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()

        @Throws(Exception::class)
        fun getStreamToGallery(context: Context): OutputStream? {
                val contentValues = ContentValues().apply {
                    this.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    this.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                }
                val uri = context.contentResolver.insert(Uri.parse(path), contentValues)
                if (uri != null) {
                  return context.contentResolver.openOutputStream(uri)
                }

            return null
        }

    }
}

