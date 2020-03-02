package com.hfad.news.tsivileva.newschannel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.habr.HabrContent
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.model.proger.ProgerContent
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedContent
import kotlinx.android.synthetic.main.fragment_feed_details.view.*


val FEED = "fragment_with_feed"
val FEED_CONTENT = "fragment_with_feed_content"
val FEED_ERROR_DOWNLOADING = "fragment_with_error_downloading_feed"
val FEED_CONTENT_ERROR_DOWNLOADING = "fragment_with_error_downloading_feed_content"
val DIALOG_WITH_ERROR = "dialog_with_error"
val DIALOG_WITH_FILTER = "dialog_with_filter"
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


    fun getFeedsContentSource(link: String): FeedsContentSource {
        var sourceKind = FeedsContentSource.HABR
        if (link.contains("habr")) {
            return FeedsContentSource.HABR
        } else
            if (link.contains("proger")) {
                return FeedsContentSource.PROGER
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


    fun RemoteRepository.Factory.parseFeed(feed: MutableList<List<Any>?>): MutableList<NewsItem> {
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
        return list
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







