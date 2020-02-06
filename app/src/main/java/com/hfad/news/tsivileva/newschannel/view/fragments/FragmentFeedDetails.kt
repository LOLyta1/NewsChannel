package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.network.NetworkClientHabrDetails
import com.hfad.news.tsivileva.newschannel.presenter.HabrItemsDetailPresenter
import com.hfad.news.tsivileva.newschannel.presenter.ProgerItemsDetailPresenter
import com.hfad.news.tsivileva.newschannel.view.IView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails:Fragment(), IView{
    var newsHTTP: String?=null
    var imageHTTP:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsHTTP=arguments?.getString("http")
        imageHTTP=arguments?.getString("img")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_feed_details,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_details_progress_bar.visibility=View.VISIBLE
        view.news_details_scroll_view.visibility=View.GONE
        view.news_details_swipe_layout.setOnRefreshListener { loadNews() }
        loadNews()

    }

    private fun loadNews(){
        if(newsHTTP?.contains("habr.com")==true){
            HabrItemsDetailPresenter(this,newsHTTP).getNews(true)
        }else
            if(newsHTTP?.contains("tproger.ru")==true){
                ProgerItemsDetailPresenter(this,newsHTTP).getNews(true)
            }
    }

    override fun showNews(newsItem: NewsItem?) {
        view?.news_details_scroll_view?.visibility=View.VISIBLE
        view?.news_details_progress_bar?.visibility=View.GONE

        view?.news_details_swipe_layout?.isRefreshing=false

        view?.news_details_text_view?.text=newsItem?.summarry
        view?.news_details_date_text_view?.text=newsItem?.date
        view?.news_details_title_text_view?.text=newsItem?.title
        view?.news_details_link_text_view?.text=newsHTTP
        Picasso.get().load(imageHTTP).placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(view?.news_details_image_view);
    }

    override fun showError(er: Throwable?) {
        Toast.makeText(context,
                       resources.getText(R.string.dialog_network_connection_lost_text),
                        Toast.LENGTH_LONG).show()
        view?.news_details_swipe_layout?.isRefreshing=false
    }

    override fun showComplete() {
     }


}