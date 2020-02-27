package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogFilterFeeds
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogSortFeeds

import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        NewsListAdapter.INewsItemClickListener,
        DialogNetworkError.IDialogListener,
        FragmentNetworkError.IErrorFragmentListener,
        DialogSortFeeds.IDialogSortFeedsClickListener,
        DialogFilterFeeds.IDialogFilterFeedsListener {

    private lateinit var viewModel: FeedViewModel
    private var newsList = mutableListOf<NewsItem>()
    private var newsAdapter: NewsListAdapter = NewsListAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter.setListener(this)

        view.swipe_container?.isRefreshing = true
        (activity as AppCompatActivity).supportActionBar?.show()

        viewModel.newsStore.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                newsList = it
            }
        })

        viewModel.isDownloadSuccessful.observe(viewLifecycleOwner, Observer<Boolean> { isDownloadingSuccessful ->
            viewModel.stopDownload()
            if (isDownloadingSuccessful) {
                logIt("FragmentFeed", " viewModel.news.observe", "загрузка прошла успешно, пришло ${newsList.count()} элементов", DEBUG_LOG)
                if (newsList.isNotEmpty() && isNotEmptyNewsList(newsList)) {
                    newsAdapter.setmList(newsList)
                    removeFragmentError(childFragmentManager, FEED_ERROR_DOWNLOADING)
                }
                view.swipe_container?.isRefreshing = false
            } else {
                showErrorFragment(childFragmentManager, R.id.news_error_container, FEED_ERROR_DOWNLOADING)
                DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                view.swipe_container?.isRefreshing = true
            }
        })
        view.news_resycler_view?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }

        view.swipe_container?.setOnRefreshListener {
            viewModel.cleareCache()
            viewModel.downloadFeeds(FeedsSource.BOTH)
        }
        viewModel.downloadFeeds(FeedsSource.BOTH)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reload_feeds_item_menu -> {
                view?.swipe_container?.isRefreshing = true
                viewModel.cleareCache()
                viewModel.downloadFeeds(FeedsSource.BOTH)
            }
            R.id.filter_feeds_item_menu -> {
                DialogFilterFeeds().show(childFragmentManager, DIALOG_WITH_FILTER)
            }
            R.id.sort_feeds_item_menu -> {
                DialogSortFeeds().show(childFragmentManager, DIALOG_WITH_SORT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val text = "Есть ли у news подписчики? -  ${viewModel.newsStore.hasActiveObservers()}"
        logIt("FragmentFeed", "onResume", text, DEBUG_LOG)
    }

    override fun onDestroyView() {

        super.onDestroyView()
        logIt("FragmentFeed", "onDestroyView", "вызван onDestroyView", DEBUG_LOG)

    }

    override fun onDestroy() {
        super.onDestroy()
        val text = "Есть ли у news подписчики? -  ${viewModel.newsStore.hasActiveObservers()}"
        logIt("FragmentFeed", "onDestroy", text, DEBUG_LOG)
    }

    override fun onNewsClick(position: Int?) {
        Log.d(DEBUG_LOG, "onNewsClick[$position]")
        val detailsFragment = FragmentFeedContent()
        val bundle = Bundle()

        if (position != null) {
            viewModel.newsStore.value?.let {
                if (it.isNotEmpty()) {
                    bundle.putString("url", it.get(position).link)
                } else {
                    if (!newsList.isNullOrEmpty()) {
                        bundle.putString("url", newsList.get(position).link)
                    }
                }
                detailsFragment.arguments = bundle
                parentFragmentManager.beginTransaction().replace(R.id.container, detailsFragment, FEED_CONTENT).addToBackStack(FEED_CONTENT).commit()
            }
        }
    }

    override fun onDialogReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        viewModel.downloadFeeds(FeedsSource.BOTH)
    }

    override fun onDialogCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        showErrorFragment(childFragmentManager, R.id.news_error_container, FEED_ERROR_DOWNLOADING)
        view?.swipe_container?.isRefreshing = false
    }

    override fun onFragmentErrorReloadButtonClick() {
        viewModel.cleareCache()
        viewModel.downloadFeeds(FeedsSource.BOTH)

        view?.swipe_container?.isRefreshing = true
    }

    override fun onDialogSortClick(sortKind: Sort) {
        val _list= mutableListOf<NewsItem>()
        _list.addAll(newsAdapter.list)
        sortNewsList(_list, sortKind)
        newsAdapter.setmList(_list)

    }



    override fun onFilterButtonClick(sourceKind: FeedsSource, isNeedCleareCache: Boolean) {
        if (isNeedCleareCache) {
            viewModel.cleareCache()
            viewModel.downloadFeeds(sourceKind)
        }
        if (sourceKind != FeedsSource.BOTH && newsList.isNotEmpty()) {
            val _tempList=newsList.filter( { it.sourceKind==sourceKind }).toMutableList()
            if(_tempList.any({ it.sourceKind==sourceKind })){
                newsAdapter.setmList(_tempList)
            }else{
                viewModel.cleareCache()
                viewModel.downloadFeeds(sourceKind)
            }
        } else
            if(sourceKind == FeedsSource.BOTH && newsList.isNotEmpty()){
                if(newsList.any({it.sourceKind==FeedsSource.HABR}) && newsList.any({it.sourceKind==FeedsSource.PROGER})){
                    newsAdapter.setmList(newsList)
                }else{
                    viewModel.cleareCache()
                    viewModel.downloadFeeds(FeedsSource.BOTH)
                }
        }
    }

}

