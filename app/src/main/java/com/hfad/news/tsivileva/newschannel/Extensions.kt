package com.hfad.news.tsivileva.newschannel

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
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedDetails
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed_details.view.*
import java.lang.Exception
import java.lang.NullPointerException


fun FragmentFeed.getNewsRecyclerAdapter(): NewsListAdapter {
    return this.view?.news_resycler_view?.adapter as NewsListAdapter
}


fun String?.toNonNullString(): String {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
    }
}

fun FragmentFeedDetails.getViewModel(activity:FragmentActivity?) : NewsContentViewModel{
    if(activity!=null){
        return ViewModelProviders.of(activity).get(NewsContentViewModel::class.java)
    }else{
        val e=Exception("ошибка создания View model")
        e.printStackTrace()
        throw Exception(e)
    }
}

fun FragmentFeed.getViewModel(activity:FragmentActivity?) : NewsViewModel{
    if(activity!=null){
        return ViewModelProviders.of(activity).get(NewsViewModel::class.java)
    }else{
        val e=Exception("ошибка создания View model")
        e.printStackTrace()
        throw Exception(e)
    }
}

fun Int?.toNonNullInt(): Int {
    if (this == null) {
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    } else {
        return this
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

fun findNew(array: MutableList<NewsItem>, url: String?): NewsItem? {
    array?.forEach {
        if (it.link?.compareTo(url.toNonNullString()) == 0) {
            return it
        }
    }
  return null
}