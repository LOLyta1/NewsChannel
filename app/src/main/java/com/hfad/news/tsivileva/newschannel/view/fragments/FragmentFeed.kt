package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError

import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        DialogError.INetworkDialogListener,
        NewsListAdapter.IClickListener,
        FragmentNetworkError.IErrorEventListener{

    lateinit var viewModel: FeedViewModel

    override fun onReloadButtonClick() {
        viewModel.loadAllNews()
        view?.swipe_container?.isRefreshing=true
    }


    val loadingStatusObserver = Observer<Boolean> { success ->
        if (!success) {
            showErrorDialog(childFragmentManager, this, "dialog_feed_error")
            view?.swipe_container?.isRefreshing = true
        }else{
            if(success){
                hideErrorFragment(childFragmentManager, ERROR_FRAGMENT_FEED)
                view?.swipe_container?.isRefreshing = false
            }
        }
        viewModel.stopLoad()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
        viewModel.cachedList.observe(this, Observer {
            val adapter=(view?.news_resycler_view?.adapter as NewsListAdapter)
            adapter.setmList(it)
        })
        viewModel.loadStatusLiveData.observe(this, loadingStatusObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_resycler_view?.apply {
            adapter = NewsListAdapter(this@FragmentFeed)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }

        view.swipe_container?.setOnRefreshListener {
            val news=viewModel.cachedList.value
            news?.clear()
            viewModel.cachedList.value=news
            viewModel.loadAllNews()
        }

        viewModel.loadAllNews()
    }


    override fun errorDialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
        viewModel.loadAllNews()
    }

    override fun errorDialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        showErrorFragment(childFragmentManager,R.id.news_error_container, ERROR_FRAGMENT_FEED)
        view?.swipe_container?.isRefreshing = false
    }

    override fun newsClick(url: String) {
        val detailsFragment = FragmentFeedDetails()
        detailsFragment.arguments = Bundle().apply {
            putString("url", url)
        }
        val allNewsFragment = parentFragmentManager.findFragmentByTag("feed_fragment")
        parentFragmentManager.beginTransaction().remove(allNewsFragment!!).add(R.id.container, detailsFragment, "detail_fragment").addToBackStack("detail_fragment").commit()
    }


}

