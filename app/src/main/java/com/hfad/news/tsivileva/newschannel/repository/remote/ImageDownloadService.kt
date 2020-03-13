package com.hfad.news.tsivileva.newschannel.repository.remote

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.R
import okhttp3.*
import java.io.File
import java.io.IOException


class ImageDownloadService() : Service() {
    companion object {
        val CHANNEL_ID = "downloading_image"
        val NOTIFICATION_ID = 100
        val DOWNLOAD_COMMAND = 1
    }

    inner class ServiceBinder: Binder() {
        fun getService()=this@ImageDownloadService
    }

    var builder: NotificationCompat.Builder? = null
    var file: File? = null


    private fun showNotification() {
        createNotificationChannel()
        builder = getNotificationBuilder()
        startForeground(NOTIFICATION_ID, builder?.build())
    }

    private fun createNotificationChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(CHANNEL_ID, "My Background Service", NotificationManager.IMPORTANCE_NONE).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
            CHANNEL_ID
        } else {
            ""
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder? {
        return NotificationCompat.Builder(this@ImageDownloadService, CHANNEL_ID)
                .setContentTitle("Загрузка файла")
                .setSmallIcon(R.drawable.download_icon)
        // .setContentIntent(PendingIntent.getActivities(this@ImageDownloadService, 0, arrayOf(Intent(this@ImageDownloadService, MainActivity::class.java)), PendingIntent.FLAG_UPDATE_CURRENT))
        //.addAction(NotificationCompat)
    }


    override fun onBind(intent: Intent?): IBinder? {
        var url = intent?.getStringExtra("url")
        val messenger = intent?.getParcelableExtra<Messenger>("messenger")
        val fileName = intent?.getStringExtra("filename")
        try {
            showNotification()

            url?.let {
                val request = Request.Builder().url(url).build()
                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                          var message = Message().apply { this.data.putByteArray("picture", null) }
                         messenger?.send(message)
                        Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ошибка ответа от сервера${e.message}")
                        stopSelf()

                    }

                    override fun onResponse(call: Call, response: Response) {
                        var bytes = response.body?.bytes()
                        var path = saveIntoFile(fileName, bytes)
                        stopSelf()
                        var message = Message().apply {
                            this.data.putString("picture", file?.path)
                        }
                        messenger?.send(message)
//                        Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ответ от сервера ${response.body?.bytes().toString()}")
                        //printHeadersAndContent(response)
                    }
                })
            }
        } catch (ex: Exception) {
            Log.d(DEBUG_LOG, "${this.javaClass.name}.onBind() - ошибка ${ex.message}")
            var message = Message().apply { this.data.putByteArray("picture", null) }
            messenger?.send(message)
        }
        return messenger?.binder
    }


    fun saveIntoFile(fileName: String?, byteArray: ByteArray?): File? {
        val dir = "${this.externalMediaDirs?.get(0)}/${fileName}"
        file = File(dir)

        val notificationManager = NotificationManagerCompat.from(this@ImageDownloadService)
        try {
            Log.d(DEBUG_LOG, "saveIntoFile() сохранено в файл -- $dir")
            if (file!!.createNewFile()) {
                if (byteArray != null) {
                    var blockLength = byteArray.count() / 10
                    val lastLenght = byteArray.count() % 10

                    var start = 0
                    for (i in 0 until 10) {
                        start = blockLength * i
                        if (start + blockLength < byteArray.count()) {
                            file!!.outputStream().write(byteArray, start, blockLength)
                        } else {
                            file!!.outputStream().write(byteArray, start, lastLenght)
                        }

                        notificationManager.apply {
                            builder?.setProgress(9, i, false)
                            builder?.build()?.let { this.notify(NOTIFICATION_ID, it) }
                        }
                    }
                    builder?.setContentIntent(
                            PendingIntent.getActivities(
                                    this@ImageDownloadService,
                                    0,
                                    arrayOf(Intent(Intent.ACTION_VIEW, Uri.parse(file?.path)).apply {
                                        this.type="image"
                                    }),
                                    PendingIntent.FLAG_UPDATE_CURRENT))
                    builder?.setContentText("Download complete")?.setProgress(0, 0, false)
                    builder?.build()?.let { notificationManager.notify(NOTIFICATION_ID, it) }
                    //file!!.outputStream().close()
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d(DEBUG_LOG, "saveIntoFile() ошибка ${e.message}")
            e.printStackTrace()
        }
        return file
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(DEBUG_LOG,"Service - onUnbind()")
        return super.onUnbind(intent)
    }
}
