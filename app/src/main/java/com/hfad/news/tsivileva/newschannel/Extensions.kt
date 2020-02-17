package com.hfad.news.tsivileva.newschannel

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.repository.remote.RemoteRepository
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedDetails
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed_details.view.*
import org.jsoup.nodes.Element
import java.lang.Exception
import java.lang.NullPointerException


fun FragmentFeed.getViewModel(activity: FragmentActivity?): NewsViewModel {
    if (activity != null) {
        return ViewModelProviders.of(activity).get(NewsViewModel::class.java)
    } else {
        val e = Exception("FragmentFeed-ошибка создания View model")
        e.printStackTrace()
        throw Exception(e)
    }
}

fun FragmentFeed.getRecyclerAdapter(): NewsListAdapter {
    return this.view?.news_resycler_view?.adapter as NewsListAdapter
}


fun FragmentFeedDetails.getViewModel(activity: FragmentActivity?): NewsContentViewModel {
    if (activity != null) {
        return ViewModelProviders.of(activity).get(NewsContentViewModel::class.java)
    } else {
        val e = Exception("FragmentFeedDetails-ошибка создания View model")
        e.printStackTrace()
        throw Exception(e)
    }
}

fun Fragment.showErrorDialog(activity: FragmentActivity?,
                             targetFragment: Fragment,
                             dialogTag: String) {
    val dialog = DialogError()
    dialog.setTargetFragment(targetFragment, 0)
    activity?.let {
        dialog.show(it.supportFragmentManager, dialogTag)
    }
}


fun getManager(fragmentActivity: FragmentActivity?): FragmentManager {
    if (fragmentActivity != null) {
        return fragmentActivity.supportFragmentManager
    } else {
        val exception = NullPointerException("manager is not created! ").apply { printStackTrace() }
        throw exception
    }
}


fun String?.toNonNullString(): String {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
    }
}

fun findNews(array: MutableList<NewsItem>, news:NewsItem?): Boolean {
    val newsItem=array.find { it.equals(news) }
    if(newsItem==null){
        return false
    } else
        return true
}


fun getIdInLink(link:String?) : Int? {
    link?.let{
        return Regex("[0-9]{6,8}").find(link,0)?.value?.toInt()
    }
    return null
}

fun findHabrNewsInCache(cache:MutableList<NewsItem>,link:String):NewsItem?{
    val id= getIdInLink(link)
    return cache.find { it.id==id }
}




