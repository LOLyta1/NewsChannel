package com.hfad.news.tsivileva.newschannel.repository.remote

import android.app.IntentService
import android.content.Intent
import okhttp3.*
import java.io.IOException


class ImageDownloadService(val listener: ServiceListener) : IntentService("com.hfad.news.tsivileva.newschannel.repository.remote.ImageDownloadService") {
    interface ServiceListener {
        fun onDownloadSuccessful(bytes: ByteArray)
        fun onDownloadError(e: java.lang.Exception)
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            var url = intent?.getStringExtra("url")
            url?.let {
                val request = Request.Builder().url(url).build()
                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        listener.onDownloadError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.bytes()?.let { _bytes -> listener.onDownloadSuccessful(_bytes) }
                    }
                })
            }
        } catch (ex: Exception) {
            listener.onDownloadError(ex)
        }
    }

}