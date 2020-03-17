package com.hfad.news.tsivileva.newschannel.users_classes

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before

import org.junit.Test

class GalleryTest {

    @Before
    fun setUp() {
    }

    @Test
    fun providerInsertTest(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val contentValues = ContentValues().apply {
            this.put(MediaStore.MediaColumns.DISPLAY_NAME, Gallery.fileName)
            this.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            this.put(MediaStore.MediaColumns.RELATIVE_PATH,"/")

        }
        val uri = context.contentResolver.insert(Uri.parse(Gallery.path), contentValues)
       Assert.assertNotNull(Gallery.getStreamGallery(context))
    }

    @After
    fun tearDown() {
    }
}