package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav
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
    private lateinit var newsList: List<NewsAndFav>
    private var preferenceValues: PreferenceValues? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceValues = NewsPreferenceSaver().getPreference(context)
        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        view.swipe_container?.isRefreshing = true
        recyclerAdapter.listener = this

        viewModel.downloadFeeds(preferenceValues)
        viewModel.downloading.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                newsList = it
                recyclerAdapter.list = it
                view.swipe_container.isRefreshing = false
            }
        })
        view.news_resycler_view?.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }
        view.swipe_container?.setOnRefreshListener {
            viewModel.downloadFeeds(preferenceValues)
        }
        view.error_reload_button.setOnClickListener {
            viewModel.downloadFeeds(preferenceValues)
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
                viewModel.downloadFeeds(preferenceValues)
            }
            R.id.sort_feeds_item_menu -> {
                DialogSortFeeds().show(childFragmentManager, DIALOG_WITH_SORT)
            }
            R.id.app_bar_search -> {
                (item.actionView as EditText).apply {
                    setHint(R.string.search_text)
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            recyclerAdapter.list = recyclerAdapter.list.filter { it.newsInfo.title.contains(s.toString()) }
                        }
                    })
                }

            }

            R.id.show_fav_menu_item -> {
                preferenceValues?.let {
                    if (it.showOnlyFav) {
                        item.setIcon(R.drawable.watch_fav)
                        it.showOnlyFav = false
                    } else {
                        item.setIcon(R.drawable.watch_fav_add)
                        it.showOnlyFav = true
                    }
                    viewModel.downloadFeeds(it)
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
                val newsDescription = recyclerAdapter.list.get(position)
                bundle.putParcelable("news_description", newsDescription)

                detailsFragment.arguments = bundle
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, detailsFragment, FEED_CONTENT)
                        .addToBackStack(FEED_CONTENT)
                        .commit()
            }
            R.id.add_to_fav_image_view -> {
                val isFav = recyclerAdapter.list.get(position).newsFav?.isFav
                if (isFav == null || isFav == false) {
                    viewModel.addToFavorite(recyclerAdapter.list.get(position).newsInfo?.id,preferenceValues)
                } else {
                    viewModel.removeFromFavorite(recyclerAdapter.list.get(position).newsInfo?.id,preferenceValues)
                }
            }
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()

        viewModel.downloadFeeds(preferenceValues)

    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.feeds_error_container?.visibility = View.VISIBLE
        view?.swipe_container?.isRefreshing = false
    }


    override fun onDialogSortClick(sortTypeKind: SortType, source: FeedsSource) {
        preferenceValues?.sortType = sortTypeKind
        preferenceValues?.source = source
        viewModel.downloadFeeds(preferenceValues)
        Toast.makeText(context, "Отсортировано", Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        preferenceValues?.let {
            NewsPreferenceSaver().setPreference(it, context)
        }
        super.onDestroyView()
    }
}


