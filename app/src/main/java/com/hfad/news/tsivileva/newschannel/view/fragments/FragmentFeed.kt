package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError

import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        NewsListAdapter.INewsItemClickListener,
        DialogError.IDialogListener,
        FragmentNetworkError.IErrorFragmentListener {

    private lateinit var viewModel: FeedViewModel
    private var newsItems = mutableListOf<NewsItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            printCachedMutableList("FragmentFeed", " viewModel.news.observer()", it)
            newsItems = it
        })

        viewModel.isDownloadSuccessful.observe(viewLifecycleOwner, Observer<Boolean> { isDownloadingSuccessful ->
            viewModel.stopDownload()
            if (isDownloadingSuccessful) {
                logIt("FragmentFeed"," viewModel.news.observe","загрузка прошла успешно, пришло ${newsItems.count()} элементов", DEBUG_LOG)
                if(newsItems.isNotEmpty()){
                    val adapter = (view?.news_resycler_view?.adapter as NewsListAdapter)
                    adapter.setmList(newsItems)
                }
                removeFragmentError(childFragmentManager, FEED_ERROR_DOWNLOADING)
                view?.swipe_container?.isRefreshing = false
            } else {
                DialogError().apply { isCancelable = false }.show(childFragmentManager, DIALOG_WITH_ERROR)
                view?.swipe_container?.isRefreshing = true
            }
        })
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_resycler_view?.apply {
            adapter = NewsListAdapter(this@FragmentFeed)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }
        viewModel.loadAllNews()

        view.swipe_container?.setOnRefreshListener {
            viewModel.cleareCache()
            viewModel.loadAllNews()
        }
    }

    override fun onNewsClick(position: Int?) {
        Log.d(DEBUG_LOG, "onNewsClick[$position]")
        position?.let {
            val url = viewModel.news.value?.get(position)?.link
            val detailsFragment = FragmentFeedContent().apply {
                arguments = Bundle().apply { putString("url", url) }
            }
            parentFragmentManager.beginTransaction().replace(R.id.container, detailsFragment, FEED_CONTENT).addToBackStack(FEED_CONTENT).commit()
        }
    }

    override fun onDialogReloadClick(dialog: DialogError) {
        dialog.dismiss()
        viewModel.loadAllNews()
    }

    override fun onDialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        showErrorFragment(childFragmentManager, R.id.news_error_container, FEED_ERROR_DOWNLOADING)
        view?.swipe_container?.isRefreshing = false
    }

    override fun onFragmentErrorReloadButtonClick() {
        viewModel.cleareCache()
        viewModel.loadAllNews()
        view?.swipe_container?.isRefreshing = true
    }



}

