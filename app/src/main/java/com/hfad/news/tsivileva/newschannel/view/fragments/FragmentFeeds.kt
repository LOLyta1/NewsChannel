package com.hfad.news.tsivileva.newschannel.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.users_classes.FeedsSource
import com.hfad.news.tsivileva.newschannel.users_classes.Filters
import com.hfad.news.tsivileva.newschannel.users_classes.NewsPreferenceSaver
import com.hfad.news.tsivileva.newschannel.users_classes.SortType
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
    private var filters: Filters? = null

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
       /* view.swipe_container?.isRefreshing = true*/

        recyclerAdapter.listener = this
        filters = NewsPreferenceSaver().getPreference(context)

        viewModel = ViewModelProviders.of(activity!!).get(FeedViewModel::class.java)
        viewModel.downloadFeeds(filters)
        viewModel.downloading.observe(viewLifecycleOwner, Observer {
            view.swipe_container.isRefreshing = false
            recyclerAdapter.list = it
            if (it.isNotEmpty()) {
                view.feeds_error_container.visibility = View.GONE
            } else {
                view.feeds_error_container.visibility = View.VISIBLE
            }
        })

        view.news_resycler_view?.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator())
        }
        view.swipe_container?.setOnRefreshListener {
            viewModel.downloadFeeds(filters)
        }

        view.error_reload_button.setOnClickListener {
            (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(view.rootView.windowToken, 0)
            }
            menu?.findItem(R.id.show_fav_menu_item)?.setIcon(R.drawable.hear_empty_icon)
            menu?.findItem(R.id.app_bar_search).apply {
                this?.collapseActionView()
                (this?.actionView as EditText).setText("")
            }

            filters?.showOnlyFav = false
            viewModel.downloadFeeds(filters)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        this.menu = menu
        filters?.let {
            when (it.showOnlyFav) {
                true -> menu.findItem(R.id.show_fav_menu_item).setIcon(R.drawable.heart_icon_full)
                false -> menu.findItem(R.id.show_fav_menu_item).setIcon(R.drawable.hear_empty_icon)
            }
        }
       //(menu.findItem(R.id.app_bar_search).actionView as EditText).apply{
        //    this.textCursorDrawable=null
         //   this.setTextColor(resources.getColor(R.color.searcher_color))
       // }


        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reload_feeds_item_menu -> {
                view?.swipe_container?.isRefreshing = true
                viewModel.downloadFeeds(filters)
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
                            if (this@apply.text.toString().isNotEmpty())
                                viewModel.searchByTitle("%${s.toString()}%")
                            else {
                                viewModel.downloadFeeds(this@FragmentFeeds.filters)
                            }
                        }
                    })
                }

            }

            R.id.show_fav_menu_item -> {
                filters?.let {
                    if (it.showOnlyFav) {
                        item.setIcon(R.drawable.hear_empty_icon)
                        it.showOnlyFav = false
                    } else {
                        item.setIcon(R.drawable.heart_icon_full)
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
            R.id.news_image_view, R.id.news_title_text_view -> {
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
                val isFav = recyclerAdapter.list[position].favorite?.isFav
                if (isFav == null || isFav == false) {
                    viewModel.addToFavorite(recyclerAdapter.list[position].description.id, filters)
                } else {
                    viewModel.removeFromFavorite(recyclerAdapter.list[position].description.id, filters)
                }
            }
            R.id.news_link_text_view -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recyclerAdapter.list[position].description.link))
                val choosenIntent = Intent.createChooser(intent, "Choose application")
                startActivity(choosenIntent)
            }
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        viewModel.downloadFeeds(filters)
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.feeds_error_container?.visibility = View.VISIBLE
        view?.swipe_container?.isRefreshing = false
    }

    override fun onDialogSortClick(sort: SortType, source: FeedsSource) {
        filters?.sort = sort
        filters?.source = source
        view?.swipe_container?.isRefreshing = true
        viewModel.downloadFeeds(filters)

    }


    override fun onDestroyView() {
        filters?.let { NewsPreferenceSaver().setPreference(it, context) }
        super.onDestroyView()
    }
}


