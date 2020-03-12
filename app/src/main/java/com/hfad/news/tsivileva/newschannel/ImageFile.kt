package com.hfad.news.tsivileva.newschannel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

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

    fun saveImageFile(context: Context?, source:Bitmap, name:String): String? {
     return MediaStore.Images.Media.insertImage(context?.contentResolver,source,name,"${name}_$source")
    }

}