package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails : Fragment() {
    private var contentUrl: String? = null


    private lateinit var viewModel: NewsContentViewModel

    private val loadingHabrObserver = Observer<NewsItem> {
       if(it.id!=null){
             showNews(it)
           loadingBar(hidden = true)
       }
    }
   private val loadingStatusObserver = Observer<Boolean>{ isSucess ->
        if(isSucess){
            loadingBar(hidden = true)
            view?.news_details_swipe?.isRefreshing = false
        }else{
            view?.news_details_swipe?.isRefreshing = true
          Toast.makeText(context,"Произошла ошибка при загрузке данных", Toast.LENGTH_LONG).show()
        }
       viewModel.stopLoad()
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentUrl = arguments?.getString("url")

        viewModel = getNewsContentViewModel()
        viewModel.newContentLiveData.observe(this, loadingHabrObserver)
        viewModel.loadingNewsSuccessful.observe(this, loadingStatusObserver)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingBar(hidden = false)
        loadNewsContent()
        view.news_details_swipe.setOnRefreshListener { loadNewsContent() }
    }


    private fun loadNewsContent() {
        val url = contentUrl.toNonNullString()

       var newsItem= findNew(viewModel.cached,contentUrl)
       Log.d("my_log","КЭШ:")
        viewModel.cached.forEach{
           print(it)
        }
        if(newsItem==null){
            Log.d("my_log","в кэше нет элемента с ссылкой ${contentUrl}")
            if (url.contains("habr.com")) {
                viewModel.loadHabrContent(url)
            } else {
                viewModel.loadProgerContent(url)
            }
       }
        else{
            Log.d("my_log","в кэше есть элемент с ссылкой ${contentUrl} - ${newsItem}")

            loadingBar(hidden = true)
            showNews(newsItem)
        }
    }

    fun showNews(newsItem: NewsItem?) {
        view?.news_details_scroll_view?.visibility = View.VISIBLE
        view?.news_details_progress_bar?.visibility = View.GONE
        view?.news_details_swipe?.isRefreshing = false
        view?.news_details_text_view?.text = newsItem?.content
        view?.news_details_date_text_view?.text = newsItem?.date
        view?.news_details_title_text_view?.text = newsItem?.title
        view?.news_details_link_text_view?.text = newsItem?.link

        newsItem?.picture?.let {
            if( ! it.isEmpty()){
            Picasso.get().load(it).placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .into(view?.news_details_image_view);
        }
        }

    }
}