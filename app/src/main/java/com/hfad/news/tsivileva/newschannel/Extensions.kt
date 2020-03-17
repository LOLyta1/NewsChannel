package com.hfad.news.tsivileva.newschannel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.news.tsivileva.newschannel.users_classes.FeedsSource
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedContent
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






