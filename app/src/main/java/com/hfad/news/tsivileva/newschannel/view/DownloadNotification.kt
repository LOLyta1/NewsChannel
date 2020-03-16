package com.hfad.news.tsivileva.newschannel.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL,visibleName,NotificationManager.IMPORTANCE_DEFAULT)
            if (context != null) {
                NotificationManagerCompat.from(context).createNotificationChannel(channel)
            }
        }
    }

    fun update(progress: Int, text:String?=""){
        if(currentProgress<progress)  currentProgress=progress
        val notification=createNotificationBuilder(currentProgress)
                ?.setContentText(text)
                ?.setProgress(100,progress,false)
                ?.build()
        if (context != null && notification!=null) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)
        }
    }

    fun hideProgress(contentText:String?){
        val notification=createNotificationBuilder(currentProgress)
                ?.setContentText(contentText)
                ?.setProgress(0,0,false)
                ?.build()
        if (context != null && notification!=null) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)
        }
    }
}