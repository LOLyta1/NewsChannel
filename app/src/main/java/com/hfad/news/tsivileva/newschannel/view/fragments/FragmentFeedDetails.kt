package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.items.Sources
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails:Fragment() {
   private var newsPosition: Int?=null
  private lateinit var viewModel : NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsPosition=arguments?.getInt("index")

        if(activity!=null &&  newsPosition!=null){
            viewModel=ViewModelProviders.of(activity!!).get(NewsViewModel::class.java)
            viewModel.newsLiveData.observe(this, Observer{ t->showNews(t[newsPosition!!])} )

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_feed_details,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingUi(true)
        view.news_details_swipe_layout.setOnRefreshListener {loadNewsDetails()}
        loadNewsDetails()
    }


     private fun loadNewsDetails(){
   /*в списке загруженных Item-ов найти тот на который кликнули, и если содержимого нет, то загрузить его по ссылке*/
         if(newsPosition!=null){
           val item=viewModel.newsLiveData.value?.get(newsPosition!!)

           if(item?.content==null){
               when(item?.sourceKind){
                   Sources.HABR ->  viewModel.loadHabrNewsDetails(item)
                   Sources.TProger -> viewModel.loadProgerNewsDetails(item)
               }
           }
       }
    }

    fun showNews(newsItem:NewsItem?) {
       view?.news_details_scroll_view?.visibility=View.VISIBLE
        view?.news_details_progress_bar?.visibility=View.GONE
        view?.news_details_swipe_layout?.isRefreshing=false
        view?.news_details_text_view?.text=newsItem?.content
        view?.news_details_date_text_view?.text=newsItem?.date
        view?.news_details_title_text_view?.text=newsItem?.title
        view?.news_details_link_text_view?.text=newsItem?.link
        Picasso.get().load(newsItem?.picture).placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(view?.news_details_image_view);
    }

    private fun showLoadingUi(isNeedToShow : Boolean){
        if(isNeedToShow){
            view?.news_details_progress_bar?.visibility=View.VISIBLE
            view?.news_details_scroll_view?.visibility=View.GONE
        }else{
            view?.news_details_progress_bar?.visibility=View.GONE
            view?.news_details_scroll_view?.visibility=View.VISIBLE
        }
    }

}