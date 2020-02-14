package com.hfad.news.tsivileva.newschannel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.view.fragments.FragmentFeed
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*




fun Fragment.getNewsViewModel(): NewsViewModel {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor().newInstance()
        }
    }
    return ViewModelProvider(viewModelStore, factory).get(NewsViewModel::class.java)
}



fun Fragment.getManager(): FragmentManager {
    //TODO: supportFragmntManager
    return this.parentFragmentManager
}

fun FragmentFeed.getNewsRecyclerAdapter(): NewsListAdapter {
    return this.view?.news_resycler_view?.adapter as NewsListAdapter
}

fun FragmentFeed.loadingBar(hiden: Boolean){
    if(hiden){
        this.view?.news_resycler_view?.visibility=View.VISIBLE
        this.view?.progress_bar?.visibility=View.GONE
        this.view?.swipe_container?.isRefreshing=false
    }else{
        this.view?.news_resycler_view?.visibility=View.GONE
        this.view?.progress_bar?.visibility=View.VISIBLE
        this.view?.swipe_container?.isRefreshing=true
    }

}