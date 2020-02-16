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
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails : Fragment() {
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
        viewModel.newContentLiveData.observe(this, Observer { showNews(it) })
        viewModel.loadingNewsSuccessful.observe(this, loadingStatusObserver)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_details_swipe?.isRefreshing = true
        loadNewsContent()
        view.news_details_swipe.setOnRefreshListener { loadNewsContent() }
    }


    private fun loadNewsContent() {
        val url = contentUrl.toNonNullString()

        // Log.d("my_log", "АДРЕС КЭШa:${viewModel.cached.hashCode()}")

          var newsItem = findNews(viewModel.cachedList, contentUrl)

        Log.d("my_log", "КЭШ:")


           if (newsItem == null) {
         Log.d("my_log", "в кэше нет элемента с ссылкой ${contentUrl}")
        if (url.contains("habr.com")) {
            viewModel.loadHabrContent(url)
        } else {
            viewModel.loadProgerContent(url)
        }
            } else {
              Log.d("my_log", "в кэше есть элемент с ссылкой ${contentUrl} - ${newsItem}")
             showNews(newsItem)
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
}