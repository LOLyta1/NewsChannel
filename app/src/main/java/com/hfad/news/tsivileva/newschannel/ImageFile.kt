package com.hfad.news.tsivileva.newschannel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ImageFile {
    companion object{
        val URI=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    fun isImageFileCreated(fileName:String?, context:Context?) : Boolean{
        val imagesInStore=context?.contentResolver?.query(
                ImageFile.URI,
                arrayOf(MediaStore.Images.Media.DISPLAY_NAME),
                "MediaStore.Images.Media.DISPLAY_NAME} LIKE ? ",
                arrayOf(fileName),
                null
        )
        val count=imagesInStore?.count
        imagesInStore?.close()
        return when(count){
            0->false
            else->true
        }

    }

    fun saveIntoFile(fileName:String,byteArray: ByteArray,context: Context?): String? {
       val dir="${context?.externalMediaDirs?.get(0)}/${fileName}"
        var file=File(dir)

        try {
            Log.d(DEBUG_LOG,"saveIntoFile() сохранено в файл -- $dir")
            if(file.createNewFile()){
                file.appendBytes(byteArray)
            }
      }catch (e:Exception){
          Log.d(DEBUG_LOG,"saveIntoFile() ошибка ${e.message}")
            e.printStackTrace()
      }

        return file.path
    }
}


