package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.repository.DownloadedFeeds
import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.repository.DownloadingProgress
import com.hfad.news.tsivileva.newschannel.repository.DownloadingSuccessful
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogFilterFeeds
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogSortFeeds
import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel
import com.hfad.news.tsivileva.newschannel.view_model.Sort
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeeds() :
        Fragment(),
        NewsListAdapter.INewsItemClickListener,
        DialogNetworkError.IDialogListener,
        FragmentNetworkError.IErrorFragmentListener,
        DialogSortFeeds.IDialogSortFeedsClickListener,
        DialogFilterFeeds.IDialogFilterFeedsListener {

    private lateinit var viewModel: FeedViewModel
    private var feeds = listOf<NewsItem>()
    private var recyclerAdapter: NewsListAdapter = NewsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()
        view.swipe_container?.isRefreshing = true

        recyclerAdapter.listener = this
        viewModel.downloadFeeds()

        viewModel.downloading.observe(viewLifecycleOwner, Observer {
            when(it){
                is DownloadedFeeds->{
                    if (it.feedList.isNotEmpty()) {
                        feeds = it.feedList
                        recyclerAdapter.list = feeds
                        view.feeds_error_container.visibility=View.GONE
                        view.swipe_container?.isRefreshing = false
                    }
                }
                is DownloadingError -> {
                        view.feeds_error_container.visibility=View.VISIBLE
                        DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                        view.swipe_container?.isRefreshing = true
                }
                is DownloadingProgress -> {//it.message
                }
            }
        })

        view.news_resycler_view?.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }

        view.swipe_container?.setOnRefreshListener {
            viewModel.cleareCache()
            viewModel.downloadFeeds()
        }

        view.error_reload_button.setOnClickListener {
            viewModel.downloadFeeds()
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
                viewModel.downloadFeeds()
            }
            R.id.filter_feeds_item_menu -> {
                DialogFilterFeeds().show(childFragmentManager, DIALOG_WITH_FILTER)
            }
            R.id.sort_feeds_item_menu -> {
                DialogSortFeeds().show(childFragmentManager, DIALOG_WITH_SORT)
            }
            R.id.app_bar_search -> {
                val searchField = (item.actionView as EditText).apply{setHint(R.string.search_text)
                }
                searchField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            recyclerAdapter.list = viewModel.searchByTitle(s.toString())
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNewsClick(position: Int?) {
        val detailsFragment = FragmentFeedContent()
        val bundle = Bundle()

        if (position != null) {
            if (!feeds.isNullOrEmpty()) {
                bundle.putString("url", feeds.get(position).link)

                detailsFragment.arguments = bundle
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, detailsFragment, FEED_CONTENT)
                        .addToBackStack(FEED_CONTENT)
                        .commit()
            }
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        viewModel.downloadFeeds()
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.feeds_error_container?.visibility=View.VISIBLE
        view?.swipe_container?.isRefreshing = false
    }


    override fun onFragmentErrorReloadButtonClick() {
        viewModel.cleareCache()
        viewModel.downloadFeeds()
        view?.swipe_container?.isRefreshing = true
        view?.feeds_error_container?.visibility=View.GONE
    }

    override fun onDialogSortClick(sortKind: Sort) {
        val _list = mutableListOf<NewsItem>().apply { addAll(recyclerAdapter.list) }
        viewModel.sortNews(sortKind)
        recyclerAdapter.list = _list
    }

    override fun onDialogFilterButtonClick(sourceKind: FeedsSource, isNeedCleareCache: Boolean) {
        recyclerAdapter.list = viewModel.filterNews(sourceKind)
    }
}


