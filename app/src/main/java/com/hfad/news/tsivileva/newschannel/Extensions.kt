package com.hfad.news.tsivileva.newschannel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Source
import com.hfad.news.tsivileva.newschannel.model.habr.Habr
import com.hfad.news.tsivileva.newschannel.model.proger.Proger
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentNetworkError
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import java.lang.NullPointerException

val FEED = "fragment_with_feed"
val FEED_CONTENT = "fragment_with_feed_content"
val FEED_ERROR_DOWNLOADING = "fragment_with_error_downloading_feed"
val FEED_CONTENT_ERROR_DOWNLOADING = "fragment_with_error_downloading_feed_content"
val DIALOG_WITH_ERROR = "dialog_with_error"
val DIALOG_WITH_FILTER="dialog_with_filter"
val DIALOG_WITH_SORT="dialog_with_sort"


fun Fragment.showDialogError(manager: FragmentManager,
                             dialogTag: String) {
    val dialog = DialogNetworkError()
    dialog.show(manager, dialogTag)
}


fun String?.toNonNullString(): String {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
    }
}

fun getIdInLink(link: String?): Long? {
    link?.let {
        return Regex("[0-9]{5,8}").find(link, 0)?.value?.toLong()
    }
    return null
}

fun isNotEmptyNewsList(news:MutableList<NewsItem>):Boolean{
    news.forEach {
        if (it.id==null) return false
    }
    return true
}

fun showErrorFragment(fragmentManager: FragmentManager, containerId: Int, tag: String) {
    val fragment = FragmentNetworkError()
     if (fragmentManager.findFragmentByTag(tag) == null) {
        fragmentManager.beginTransaction().add(containerId, fragment, tag).addToBackStack(tag).commit()
    }
}

fun removeFragmentError(fragmentManager: FragmentManager, tag: String) {
    val fragment = fragmentManager.findFragmentByTag(tag)
    fragment?.let {
        fragmentManager.beginTransaction().remove(fragment).commit()
    }
    printFragmentsInManager(fragmentManager)
}

fun sortNewsList(list:MutableList<NewsItem>,sortKind: Sort){
    when(sortKind){
        Sort.BY_ABC_ASC->{list.sortBy { it.title }}
        Sort.BY_ABC_DESC->{list.sortByDescending { it.title }}
        Sort.BY_DATE_ASC->{list.sortBy { it.date }}
        Sort.BY_DATE_DESC->{list.sortByDescending { it.date }}
    }
}

fun getSourceKind(link:String?) : Source?{
    link?.let {
        if( it.contains("habr.com")) {
            return Source.HABR
        }else
            if( it.contains("tproger.ru")) {
                return Source.PROGER
            }
    }
    return null
}


fun parsHabrFeed(habr:Habr) : List<NewsItem> {
    val list= mutableListOf<NewsItem>()
    habr.items?.forEach {
        var newsItem = NewsItem()
        newsItem.sourceKind = Source.HABR
        newsItem.id = getIdInLink(it.link)
        newsItem.link = it.link
        newsItem.picture = it.image
        newsItem.title = it.title
        newsItem.date = it.date
        list.add(newsItem)
    }
    return list
}


fun parsProgerFeed(proger:Proger) : List<NewsItem> {
    val list= mutableListOf<NewsItem>()
    proger.channel?.items?.forEach {
        var newsItem = NewsItem()
        newsItem.sourceKind = Source.PROGER
        newsItem.id = getIdInLink(it.guid)
        newsItem.link = it.link
        newsItem.title = it.title
        newsItem.date = it.date
        newsItem.picture = "https://tproger.ru/apple-touch-icon.png"
        list.add(newsItem)
    }
    return list
}


 fun parseFeed(feed: MutableList<Any?>): List<NewsItem> {
    val list= mutableListOf<NewsItem>()
     feed.forEach {
         when (it) {
             is Habr -> {
                 it.items?.forEach {
                     var newsItem = NewsItem()
                     newsItem.sourceKind = Source.HABR
                     newsItem.id = getIdInLink(it.link)
                     newsItem.link = it.link
                     newsItem.picture = it.image
                     newsItem.title = it.title
                     newsItem.date = it.date
                     list.add(newsItem)
                 }
             }
             is Proger -> {
                 it.channel?.items?.forEach {
                     var newsItem = NewsItem()
                     newsItem.sourceKind = Source.PROGER
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






