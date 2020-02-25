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
import com.hfad.news.tsivileva.newschannel.adapter.Source
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
    private var newsItems = mutableListOf<NewsItem>()

    private var newsAdapter: NewsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel.news.observe(viewLifecycleOwner, Observer {
            newsItems = it
        })

        viewModel.isDownloadSuccessful.observe(viewLifecycleOwner, Observer<Boolean> { isDownloadingSuccessful ->
            viewModel.stopDownload()
            if (isDownloadingSuccessful) {
                logIt("FragmentFeed", " viewModel.news.observe", "загрузка прошла успешно, пришло ${newsItems.count()} элементов", DEBUG_LOG)
                if (newsItems.isNotEmpty() && isNotEmptyNewsList(newsItems)) {
                    val adapter = (view?.news_resycler_view?.adapter as NewsListAdapter)
                    adapter.setmList(newsItems)
                    view?.swipe_container?.isRefreshing = false
                }
                removeFragmentError(childFragmentManager, FEED_ERROR_DOWNLOADING)
            } else {
                DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                view?.swipe_container?.isRefreshing = true
            }
        })
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.swipe_container?.isRefreshing = true
        newsAdapter = NewsListAdapter(this)

        view.news_resycler_view?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }

        viewModel.loadAllNews()
        view.swipe_container?.setOnRefreshListener {
            viewModel.cleareCache()
            viewModel.loadAllNews()
        }

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
                viewModel.loadAllNews()
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
        val text = "Есть ли у news подписчики? -  ${viewModel.news.hasActiveObservers()}"
        logIt("FragmentFeed", "onResume", text, DEBUG_LOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        logIt("FragmentFeed", "onDestroyView", "вызван onDestroyView", DEBUG_LOG)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.news.removeObservers(viewLifecycleOwner)
        viewModel.isDownloadSuccessful.removeObservers(viewLifecycleOwner)
        val text = "Есть ли у news подписчики? -  ${viewModel.news.hasActiveObservers()}"
        logIt("FragmentFeed", "onDestroy", text, DEBUG_LOG)
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

    override fun onDialogReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        viewModel.loadAllNews()
    }

    override fun onDialogCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        showErrorFragment(childFragmentManager, R.id.news_error_container, FEED_ERROR_DOWNLOADING)
        view?.swipe_container?.isRefreshing = false
    }

    override fun onFragmentErrorReloadButtonClick() {
        viewModel.cleareCache()
        viewModel.loadAllNews()
        view?.swipe_container?.isRefreshing = true
    }

    override fun onSortClick(sortKind: Sort) {
        view?.swipe_container?.isRefreshing = true
        viewModel.sort(sortKind)
    }

    override fun onFilterButtonClick(sourceKind: Source) {
    }
}

