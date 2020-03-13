package com.hfad.news.tsivileva.newschannel.repository.remote

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.activity.MainActivity
import com.hfad.news.tsivileva.newschannel.printHeadersAndContent
import okhttp3.*
import java.io.File
import java.io.IOException


class ImageDownloadService() : Service() {
    companion object {
        val CHANNEL_ID = "downloading_image"
        val NOTIFICATION_ID = 100
        val DOWNLOAD_COMMAND = 1
    }

    private var _builder: NotificationCompat.Builder? = null

    override fun onBind(intent: Intent?): IBinder? {
        var url = intent?.getStringExtra("url")
        val messenger = intent?.getParcelableExtra<Messenger>("messenger")
        val fileName = intent?.getStringExtra("filename")

        try {

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Загрузка файла")
                    .setSmallIcon(R.drawable.download_icon)
                    .setContentIntent(PendingIntent.getActivities(this, 0, arrayOf(Intent(this, MainActivity::class.java)), PendingIntent.FLAG_UPDATE_CURRENT))
            _builder = builder
            startForeground(NOTIFICATION_ID, builder.build())

            url?.let {
                val request = Request.Builder().url(url).build()
                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        var message = Message().apply { this.data.putByteArray("picture", null) }
                        messenger?.send(message)
                        Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ошибка ответа от сервера${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        saveIntoFile(fileName, response.body?.bytes())
                        var message = Message().apply {
                            this.data.putByteArray("picture", response.body?.bytes())
                        }
                        messenger?.send(message)
                        Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ответ от сервера ${response.body?.bytes().toString()}")
                     //printHeadersAndContent(response)
                    }
                })
            }
        } catch (ex: Exception) {
            Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ошибка ${ex.message}")
            var message = Message().apply { this.data.putByteArray("picture", null) }
            messenger?.send(message)
        }
        return null
    }


    fun saveIntoFile(fileName: String?, byteArray: ByteArray?): String? {
        val dir = "${this.externalMediaDirs?.get(0)}/${fileName}"
        val file = File(dir)

        val notificationManager = NotificationManagerCompat.from(this@ImageDownloadService)

        try {
            Log.d(DEBUG_LOG, "saveIntoFile() сохранено в файл -- $dir")
            if (file.createNewFile()) {
                if (byteArray != null) {
                    file.appendBytes(byteArray)
                }}/*
                byteArray?.forEachIndexed { index, byte ->

                    file.outputStream().write(byteArray, index, 1)
                    byteArray.count().let { it1 ->
                        notificationManager.apply {
                            val builder = NotificationCompat.Builder(this@ImageDownloadService, CHANNEL_ID)
                                    .setContentTitle("Загрузка файла")
                                    .setSmallIcon(R.drawable.download_icon)
                                    .setContentIntent(PendingIntent.getActivities(this@ImageDownloadService, 0, arrayOf(Intent(this@ImageDownloadService, MainActivity::class.java)), PendingIntent.FLAG_UPDATE_CURRENT))
                            builder.setProgress(it1, index, true)

                            builder.build().let { this.notify(NOTIFICATION_ID, builder.build()) }
                        }

                    }

                }
            }*/
            //notification?.setContentText("Download complete")?.setProgress(0, 0, false)
            //notification?.build()?.let { notificationManager.notify(NOTIFICATION_ID, it) }
            //outputStream.close()

        } catch (e: java.lang.Exception) {
            Log.d(DEBUG_LOG, "saveIntoFile() ошибка ${e.message}")
            e.printStackTrace()
        }
        return file.path
    }
}