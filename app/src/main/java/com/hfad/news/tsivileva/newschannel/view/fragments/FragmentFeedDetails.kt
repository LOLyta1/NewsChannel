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
        Log.d("mylog","FragmentFeedDetails - пришла ссылка ${newsHTTP}")

        Log.d("cikl","onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_feed_details,container,false)
        Log.d("cikl","onCreateView()")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HabrItemsDetailPresenter(this,newsHTTP).getNews(true)


        /* view.news_details_date_text_view?.text=news?.date
         Picasso.get().load(news?.picture).placeholder(R.drawable.no_photo)
                 .error(R.drawable.no_photo)
                 .into(view.news_details_image_view);
         view.news_details_link_text_view?.text=news?.link
         view.news_details_text_view?.text=news?.summarry*/
    }



    override fun showNews(newsItem: NewsItem?) {
        view?.news_details_text_view?.text=newsItem?.summarry
        view?.news_details_date_text_view?.text=newsItem?.date
        view?.news_details_title_text_view?.text=newsItem?.title
        view?.news_details_link_text_view?.text=newsHTTP
        Picasso.get().load(imageHTTP).placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(view?.news_details_image_view);
        Log.d("cikl","onViewCreated()")

    }

    override fun showError(er: Throwable?) {
        Toast.makeText(context,er?.message,Toast.LENGTH_LONG).show()
    }

    override fun showComplete() {
        Toast.makeText(context,"download is complete",Toast.LENGTH_LONG).show()


    }

    override fun onDetach() {
        super.onDetach()
        Log.d("cikl","onDetach()")

    }

    override fun onPause() {
        super.onPause()
        Log.d("cikl","onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("cikl","onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.news_details_text_view?.text=""
        view?.news_details_date_text_view?.text=""
        Log.d("cikl","onDestroy()")
    }
}