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
        NewsListAdapter.IClickListener,
        DialogError.IDialogListener,
        FragmentNetworkError.IErrorFragmentListener {

    private lateinit var viewModel: FeedViewModel

    val loadingStatusObserver = Observer<Boolean> { isDownloadingSuccessful ->
        viewModel.stopLoad()
        if (isDownloadingSuccessful) {
            removeFragmentError(childFragmentManager, FRAGMENT_WITH_ERROR_DOWNLOADING_FEED)
            view?.swipe_container?.isRefreshing = false
        } else {
            showDialogError(childFragmentManager, this, "dialog_feed_error")
            view?.swipe_container?.isRefreshing = true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
        viewModel.cachedList.observe(this, Observer {
            val adapter = (view?.news_resycler_view?.adapter as NewsListAdapter)
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
            addItemDecoration(NewsListDecorator())
        }

        //TODO: поправить refresh
        view.swipe_container?.setOnRefreshListener {
            val news = viewModel.cachedList.value
            news?.clear()
            viewModel.cachedList.value = news
            viewModel.loadAllNews()
        }
        viewModel.loadAllNews()
    }

    override fun onNewsClick(url: String) {

        val detailsFragment = FragmentFeedContent()
        detailsFragment.arguments = Bundle().apply {
            putString("url", url)
        }
        val newsFragment = parentFragmentManager.findFragmentByTag(FRAGMENT_WITH_FEED)


        parentFragmentManager.
                beginTransaction().
                replace(R.id.container, detailsFragment, FRAGMENT_WITH_FEED_CONTENT).
                addToBackStack(FRAGMENT_WITH_FEED_CONTENT).
                commit()
    }

    override fun onDialogReloadClick(dialog: DialogError) {
        dialog.dismiss()
        viewModel.loadAllNews()
    }

    override fun onDialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        showErrorFragment(childFragmentManager, R.id.news_error_container, FRAGMENT_WITH_ERROR_DOWNLOADING_FEED)
        view?.swipe_container?.isRefreshing = false
    }

    override fun onFragmentErrorReloadButtonClick() {
        viewModel.loadAllNews()
        view?.swipe_container?.isRefreshing = true
    }

}

