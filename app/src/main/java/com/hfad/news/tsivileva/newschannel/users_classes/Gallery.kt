package com.hfad.news.tsivileva.newschannel.users_classes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.OutputStream

class Gallery {
    companion object {
        var filepath :String?= ""
    }

    @Throws(Exception::class)
    fun getStream(context: Context): OutputStream? {

         val path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()

        val contentValues = ContentValues().apply {
            this.put(MediaStore.MediaColumns.DISPLAY_NAME, "temp${getLastImageId(context)}.png")
            this.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        }
        val uri = context.contentResolver.insert(Uri.parse(path), contentValues)
        if (uri != null) {
            filepath = getRelativePath(context,uri)
            return context.contentResolver.openOutputStream(uri)
        }
        return null
    }

    private fun getLastImageId(context: Context): Int {
        var lastId: Int =-1
        context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.ImageColumns._ID).apply {
            this?.let{_cursor->
               if(_cursor.moveToLast()){
                   val index = _cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                   lastId = _cursor.getInt(index)
               }
            }
            this?.close()
        }
        return lastId
    }

    private fun getRelativePath(context: Context, uri: Uri): String? {
        var relPath: String? = ""
        context.contentResolver.query(uri, null, null, null, null).apply {
            this?.let{ _cursor: Cursor ->
                if( _cursor.moveToFirst()){
                    val index = _cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    relPath =  _cursor.getString(index)
                    _cursor.close()
                }
            }
            this?.close()
        }
        return relPath
    }


}

