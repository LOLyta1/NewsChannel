package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.view.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.view.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogSortFeeds
import com.hfad.news.tsivileva.newschannel.view_model.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeeds() :
        Fragment(),
        NewsListAdapter.INewsItemClickListener,
        DialogNetworkError.IDialogListener,
        DialogSortFeeds.IDialogSortFeedsClickListener {

    private lateinit var viewModel: FeedViewModel
    private var recyclerAdapter: NewsListAdapter = NewsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        view.swipe_container?.isRefreshing = true
        recyclerAdapter.listener = this

        viewModel.downloadFeeds()
        viewModel.downloading.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DownloadingSuccessful -> {
                    if (it.data != null) {
                        recyclerAdapter.list = it.data
                    }
                    view.feeds_error_container.visibility = View.GONE
                    view.swipe_container?.isRefreshing = false
                }
                is DownloadingError -> {
                    view.feeds_error_container.visibility = View.VISIBLE
                    DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                    view.swipe_container?.isRefreshing = true
                    if (it.cachedData != null) {
                        recyclerAdapter.list = it.cachedData
                    }
                }
            }
        })

        view.news_resycler_view?.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }

        view.swipe_container?.setOnRefreshListener {
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
                viewModel.downloadFeeds()
            }
            R.id.sort_feeds_item_menu -> {
                DialogSortFeeds().show(childFragmentManager, DIALOG_WITH_SORT)
            }
            R.id.app_bar_search -> {
                val searchField = (item.actionView as EditText).apply {
                    setHint(R.string.search_text)
                }
                searchField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        viewModel.searchByTitle(s.toString())
                    }
                })
            }

            R.id.show_fav_menu_item->{
                if(recyclerAdapter.list.all { it.newsFav?.isFav==true}){
                    item.setIcon(R.drawable.watch_fav)
                    viewModel.downloadFeeds()
                }else{
                    viewModel.showFav()
                    item.setIcon(R.drawable.watch_fav_add)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNewsClick(position: Int, view: View) {
        when (view.id) {
            R.id.news_image_view -> {
                val detailsFragment = FragmentFeedContent()
                val bundle = Bundle()
                val list = recyclerAdapter.list.get(position)
                bundle.putParcelable("news_description", list)
                detailsFragment.arguments = bundle
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, detailsFragment, FEED_CONTENT)
                        .addToBackStack(FEED_CONTENT)
                        .commit()
            }
            R.id.fav_image_view -> {
                Log.d(DEBUG_LOG,"нажали на картинку")
                val isFav=recyclerAdapter.list.get(position).newsFav?.isFav
                Log.d(DEBUG_LOG," позиция в списке изюранных? ${recyclerAdapter.list.get(position).newsFav?.isFav}")
               if(isFav==null || isFav==false){
                   recyclerAdapter.list.get(position).newsInfo?.id?.let { it1 -> viewModel.addToFavorite(it1) }
               }else{
                   recyclerAdapter.list.get(position).newsInfo?.id?.let { it1 -> viewModel.removeFromFavorite(it1) }
               }
            }
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        viewModel.downloadFeeds()
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.feeds_error_container?.visibility = View.VISIBLE
        view?.swipe_container?.isRefreshing = false
    }


    override fun onDialogSortClick(sortTypeKind: SortType, source: FeedsSource) {
        viewModel.sortNews(sortTypeKind, source)
//        var tempList = viewModel.filterNews(filter)
//        tempList = viewModel.sortNews(sortKind, tempList.toMutableList())
//        recyclerAdapter.list = tempList
    }
}


