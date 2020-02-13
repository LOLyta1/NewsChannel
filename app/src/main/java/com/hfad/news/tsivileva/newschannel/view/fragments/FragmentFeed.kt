package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.adapter.AdapterNews
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsDecorator
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNet
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        DialogNet.INetworkDialogListener,
        AdapterNews.IClickListener {

    private var swiper: SwipeRefreshLayout? = null
    lateinit var viewModel: NewsViewModel

    val dataObserver = Observer<MutableList<NewsItem>> {
        view?.new_list_resycler_view?.visibility = View.VISIBLE
        val _adapter = view?.new_list_resycler_view?.adapter as AdapterNews
        _adapter.setmList(it)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            val manager=activity.supportFragmentManager
            viewModel=ViewModelProviders.of(activity).get(NewsViewModel::class.java)
            viewModel.newsLiveData.observe(this,dataObserver)

            viewModel.newsLoaded.observe(this, Observer {
               if(it==true){
                   Toast.makeText(context,"загрузка завершена",Toast.LENGTH_LONG).show()
                   showLoadingUi(false)
               }
            })

            viewModel.error.observe(this, Observer { DialogNet().apply {
                setTargetFragment(this@FragmentFeed,10)
                show(manager,"dialog_error")
            }
               })
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_progress_bar.visibility = View.VISIBLE

        view.new_list_resycler_view?.apply {
            adapter = AdapterNews(this@FragmentFeed)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }
        swiper = view.swipe_container
        swiper?.setOnRefreshListener {
                viewModel.cleareNews()
                viewModel.loadNews()
            }

        /*кэширование - если не было загружено ни 1 элемента, загрузить*/
        val count= viewModel.newsLiveData.value?.count()
        if(count!=null && count==0){
            viewModel.loadNews()
            showLoadingUi(true)
        }else if( count!=null && count>0){
            showLoadingUi(false)
        }
    }

    override fun uploadClick(dialog: DialogNet) {
        dialog.dismiss()
        showLoadingUi(true)
        viewModel.loadNews()
    }

    override fun cancelClick(dialog: DialogNet) {
        dialog.dismiss()
        showLoadingUi(false)
        viewModel.stopSubscription()
        viewModel.newsLiveData.removeObservers(this)
    }


    override fun newsClick(position: Int) {
         val fragment = FragmentFeedDetails()
          fragment.arguments= Bundle().apply {
              putInt("index", position)

          }
          activity?.supportFragmentManager?.
                  beginTransaction()?.
                  replace(R.id.container, fragment, "detail_fragment")?.
                  addToBackStack("detail_fragment")?.
                  commit()
    }


    fun showLoadingUi(needShow: Boolean){
        if(needShow){
            view?.swipe_container?.isRefreshing = true
            view?.news_progress_bar?.visibility = View.VISIBLE
        }else{
            view?.swipe_container?.isRefreshing = false
            view?.news_progress_bar?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopSubscription()
        viewModel.subscription.removeObservers(this)
    }
}