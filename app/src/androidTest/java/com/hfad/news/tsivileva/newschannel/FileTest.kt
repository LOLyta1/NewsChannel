package com.hfad.news.tsivileva.newschannel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Picture
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FileTest {
    var context:Context?=null
    @Before
    fun setUp() {
        context=ApplicationProvider.getApplicationContext<Context>()
    }

    @Test
    fun isImageFileCreatedTest(){
        print(ImageFile().saveImageFile(context, Bitmap.createBitmap(Picture()),"1.txt"))
    }

    @Test
    fun saveFileTest(){

    }
}