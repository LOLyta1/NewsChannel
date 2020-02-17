package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails : Fragment(), DialogError.INetworkDialogListener {
    private var contentUrl: String? = null

    private lateinit var viewModel: NewsContentViewModel

    private val loadingStatusObserver = Observer<Boolean> { isSucess ->
        if (!isSucess) {
            showErrorDialog(activity, this, "dialog_details_error")
        }
        view?.news_details_swipe?.isRefreshing = false
        viewModel.stopLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentUrl = arguments?.getString("url")
        viewModel = getViewModel(activity)
        viewModel.newContentLiveData.observe(this, Observer { showNews(it)})
        viewModel.loadingNewsSuccessful.observe(this, loadingStatusObserver)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNewsContent()
        view.news_details_swipe.setOnRefreshListener { loadNewsContent() }
    }


    private fun loadNewsContent() {
        var newsInCached = findHabrNewsInCache(viewModel.cachedList, contentUrl.toNonNullString())

        if (newsInCached == null) {
            view?.news_details_swipe?.isRefreshing = true
            viewModel.cleareNewsContent()
            contentUrl?.let {
                if (it.contains("habr.com")) {
                    viewModel.loadHabrContent(it)
                } else {
                    viewModel.loadProgerContent(it)
                }
            }
        } else {
            viewModel.newContentLiveData.value=newsInCached
            view?.news_details_swipe?.isRefreshing = false
        }

    }

    fun showNews(newsItem: NewsItem?) {
        view?.news_details_text_view?.text = newsItem?.content
        view?.news_details_date_text_view?.text = newsItem?.date
        view?.news_details_title_text_view?.text = newsItem?.title
        view?.news_details_link_text_view?.text = newsItem?.link

        newsItem?.picture?.let {
            if (!it.isEmpty()) {
                Picasso.get().load(it).placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(view?.news_details_image_view);
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopLoad()
    }

    override fun dialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
        viewModel.loadProgerContent(contentUrl.toNonNullString())

    }

    override fun dialogCancelClick(dialog: DialogError) {
dialog.dismiss()
    }
}