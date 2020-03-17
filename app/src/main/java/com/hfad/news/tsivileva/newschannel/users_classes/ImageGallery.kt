package com.hfad.news.tsivileva.newschannel.users_classes

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.roundToInt

class ImageGallery {
    companion object {
        val path = Environment.getExternalStorageDirectory()
        val fileName="temp.png"

        fun saveInExternalStorage(stream:FileOutputStream){
        }

        fun updateImageInGallery(context: Context) {
            MediaStore.Images.Media.insertImage(context.contentResolver, "${path}/$fileName", fileName, "downloaded")
        }

    }
}

