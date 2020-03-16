package com.hfad.news.tsivileva.newschannel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedContent
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeeds
import kotlinx.android.synthetic.main.fragment_feed_details.view.*
import java.lang.reflect.InvocationTargetException

const val DATABASE_NAME = "NewsDatabase"
val FEED = "fragment_with_feed"
val FEED_CONTENT = "fragment_with_feed_content"
val DIALOG_WITH_ERROR = "dialog_with_error"
val DIALOG_WITH_SORT = "dialog_with_sort"


fun Fragment.showDialogError(manager: FragmentManager,
                             dialogTag: String) {
    val dialog = DialogNetworkError()
    dialog.show(manager, dialogTag)
}

fun FragmentFeedContent.getFeedInfo(): String {
    return "Дата: ${this.view?.news_details_date_text_view?.text}" +
            "\n Заголовок: ${this.view?.news_details_title_text_view?.text}" +
            "\nСсылка: ${this.view?.news_details_link_text_view?.text}" +
            "\nНовость: ${this.view?.news_details_text_view?.text} "
}

fun String?.toNonNullString(): String {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
    }
}

fun getIdInLink(link: String?): Long {
    val id = Regex("[0-9]{5,8}").find(link.toString(), 0)?.value?.toLong()
    return when (id) {
        null -> 0L
        else -> id
    }
}


fun getSourceByLink(link: String): FeedsSource {
    var sourceKind = FeedsSource.BOTH
    if (link.contains("habr")) {
        return FeedsSource.HABR
    } else
        if (link.contains("proger")) {
            return FeedsSource.PROGER
        } else return sourceKind
}

fun getViewModelFactory(app: Application): ViewModelProvider.NewInstanceFactory {
    return object : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            try {
                return modelClass.getConstructor(Application::class.java).newInstance(app);
            } catch (e: InstantiationException) {
                e.printStackTrace();
            } catch (e: IllegalAccessException) {
                e.printStackTrace();
            } catch (e: InvocationTargetException) {
                e.printStackTrace();
            } catch (e: NoSuchMethodException) {
                e.printStackTrace();
            }
            return super.create(modelClass)
        }
    }
}
fun FragmentFeedContent.createNotification() {
    context?.let { _context: Context ->
        this.notification = NotificationCompat.Builder(_context, this.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.download_icon)
                .setContentTitle(resources.getString(R.string.downloading_file))
                .setProgress(100,0,false)
                .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(this.NOTIFICATION_CHANNEL,"com.hfad.news.tsivileva.newschannel.file_download_channel", NotificationManager.IMPORTANCE_DEFAULT)
            NotificationManagerCompat.from(_context).createNotificationChannel(channel)
        }
        this.notification?.let{
            NotificationManagerCompat.from(_context).notify(this.NOTIFICATION_ID,it)
        }
    }
}







