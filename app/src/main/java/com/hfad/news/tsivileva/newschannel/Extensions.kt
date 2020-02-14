package com.hfad.news.tsivileva.newschannel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeedDetails
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed_details.view.*
import java.lang.NullPointerException


fun Fragment.getNewsViewModel(): NewsViewModel {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor().newInstance()
        }
    }
    return ViewModelProvider(viewModelStore, factory).get(NewsViewModel::class.java)
}

fun Fragment.getNewsContentViewModel(): NewsContentViewModel {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor().newInstance()
        }
    }
    return ViewModelProvider(viewModelStore, factory).get(NewsContentViewModel::class.java)
}

fun FragmentFeed.getNewsRecyclerAdapter(): NewsListAdapter {
    return this.view?.news_resycler_view?.adapter as NewsListAdapter
}

fun FragmentFeed.loadingBar(hidden: Boolean) {
    if (hidden) {
        this.view?.news_resycler_view?.visibility = View.VISIBLE
        this.view?.progress_bar?.visibility = View.GONE

    } else {
        this.view?.news_resycler_view?.visibility = View.GONE
        this.view?.progress_bar?.visibility = View.VISIBLE

    }
}

fun FragmentFeedDetails.loadingBar(hidden: Boolean){
    if(hidden){
        view?.news_details_progress_bar?.visibility=View.GONE
        view?.news_details_scroll_view?.visibility=View.VISIBLE
    }else{
        view?.news_details_progress_bar?.visibility=View.VISIBLE
        view?.news_details_scroll_view?.visibility=View.GONE
    }
}

fun String?.toNonNullString() : String{
    if (this==null){
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    }else{
        return this
    }
}

fun Int?.toNonNullInt() : Int {
    if (this==null){
        val exception = NullPointerException("URL is empty!").apply { printStackTrace() }
        throw exception
    }else{
        return this
    }
}

fun getManager(fragmentActivity: FragmentActivity?) : FragmentManager{
    if(fragmentActivity!=null){
        return fragmentActivity.supportFragmentManager
    }else{
        val  exception = NullPointerException("manager is not created! ").apply{printStackTrace()}
        throw exception
    }
}