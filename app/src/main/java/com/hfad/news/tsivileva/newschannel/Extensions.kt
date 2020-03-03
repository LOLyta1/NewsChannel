package com.hfad.news.tsivileva.newschannel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedContent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feed_details.view.*
import java.lang.reflect.InvocationTargetException

const val DATABASE_NAME="NewsDatabase"
const val DATABASE_TABLE_NAME="News"
const val DATABASE_ID_COLUMN="_id"
const val DATABASE_DATE_COLUMN="date"
const val DATABASE_PICTURE_COLUMN="picture"
const val DATABASE_SOURCE_COLUMN="sourceKind"
const val DATABASE_LINK_COLUMN="link"
const val DATABASE_TITLE_COLUMN="title"
const val DATABASE_CONTENT_COLUMN="content"
const val DATABASE_FAVOURITE_COLUMN="favorite"



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

    fun RemoteRepository.Factory.parsHabrFeed(habr: Habr): List<NewsItem> {
        val list = mutableListOf<NewsItem>()
        habr.items?.forEach {
            val newsItem = NewsItem()
            newsItem.sourceKind = FeedsSource.HABR
            newsItem.id = getIdInLink(it.link)
            newsItem.link = it.link
            newsItem.picture = it.image
            newsItem.title = it.title
            newsItem.date = it.date
            list.add(newsItem)
        }
        return list
    }


    fun RemoteRepository.Factory.parsProgerFeed(proger: Proger): MutableList<NewsItem> {
        val list = mutableListOf<NewsItem>()
        proger.channel?.items?.forEach {
            val newsItem = NewsItem()
            newsItem.sourceKind = FeedsSource.PROGER
            newsItem.id = getIdInLink(it.guid)
            newsItem.link = it.link
            newsItem.title = it.title
            newsItem.date = it.date
            newsItem.picture = "https://tproger.ru/apple-touch-icon.png"
            list.add(newsItem)
        }
        return list
    }


    fun RemoteRepository.Factory.parseFeed(feed:List<List<Any>?>): Observable<MutableList<NewsItem>> {
        val list = mutableListOf<NewsItem>()
        feed.forEach {
            it?.forEach {
                when (it) {
                    is Habr.HabrlItems -> {
                        val newsItem = NewsItem()
                        newsItem.sourceKind = FeedsSource.HABR
                        newsItem.id = getIdInLink(it.link)
                        newsItem.link = it.link
                        newsItem.picture = it.image
                        newsItem.title = it.title
                        newsItem.date = it.date
                        list.add(newsItem)
                    }
                    is Proger.Channel.Item -> {
                        val newsItem = NewsItem()
                        newsItem.sourceKind = FeedsSource.PROGER
                        newsItem.id = getIdInLink(it.guid)
                        newsItem.link = it.link
                        newsItem.title = it.title
                        newsItem.date = it.date
                        newsItem.picture = "https://tproger.ru/apple-touch-icon.png"
                        list.add(newsItem)
                    }
                }
            }

        }
        return Observable.fromArray(list).
                observeOn(Schedulers.io()).
                subscribeOn(AndroidSchedulers.mainThread())
    }

    fun RemoteRepository.Factory.parseHabrFeedsContent(hc: HabrContent): NewsItem {
        return NewsItem(
                title = hc.title,
                content = hc.content,
                date = hc.date,
                picture = hc.image,
                link = hc.link,
                id = hc.id,
                sourceKind = FeedsSource.HABR
        )
    }

    fun RemoteRepository.Factory.parseProgerFeedsContent(pc: ProgerContent): NewsItem {
        return NewsItem(
                title = pc.title,
                content = pc.content,
                date = pc.date,
                picture = pc.image,
                link = pc.link,
                id = pc.id,
                sourceKind = FeedsSource.PROGER)
    }



fun getViewModelFactory(app: Application) : ViewModelProvider.NewInstanceFactory {
    return object: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) : T {
            try {
                return modelClass.getConstructor(Application::class.java).newInstance(app);
            } catch (e:InstantiationException) {
                e.printStackTrace();
            } catch (e:IllegalAccessException ) {
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









