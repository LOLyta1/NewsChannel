package com.hfad.news.tsivileva.newschannel.users_classes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hfad.news.tsivileva.newschannel.DEBUG_LOG
import com.hfad.news.tsivileva.newschannel.R

class DownloadNotification(private val context:Context?) {
    private var visibleName:String="News Channel-downloading"
    private val NOTIFICATION_CHANNEL = "file_downloading"
    private val NOTIFICATION_ID = 1

   private var currentProgress=0

    init{
       createNotificationChannel()
    }

    private fun createNotificationBuilder(progress: Int): NotificationCompat.Builder? {
        return context?.let {
            NotificationCompat.Builder(it, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.download_icon)
                .setContentTitle(context.resources.getString(R.string.downloading_file))
                .setContentText("$progress%")
                .setProgress(100, progress, false)
        }

    }

    private fun createNotificationChannel(){
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL,visibleName,NotificationManager.IMPORTANCE_DEFAULT).apply {
                importance=NotificationManager.IMPORTANCE_LOW
            }
                NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    fun update(progress: Int, text:String?=""){
            Log.d(DEBUG_LOG, " ${this.javaClass.name} - update(${progress}, $text)")
            currentProgress = progress
            val notification = createNotificationBuilder(currentProgress)
                    ?.setContentText(text)
                    ?.setProgress(100, progress, false)
                    ?.build()
            if (context != null && notification != null) {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            }
    }

    fun hideProgress(contentText:String?){
        val notification=createNotificationBuilder(currentProgress)
                ?.setContentText(contentText)
                ?.setProgress(0,0,false)
                ?.build()
        if (context!= null && notification!=null) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)
        }
    }
}