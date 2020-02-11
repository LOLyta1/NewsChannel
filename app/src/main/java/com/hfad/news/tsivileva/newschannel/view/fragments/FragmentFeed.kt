package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.hfad.news.tsivileva.newschannel.view_model.SharedViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
      //  IView,
        DialogNet.INetworkDialogListener,
        AdapterNews.IClickListener {

    private var swiper: SwipeRefreshLayout? = null
    private val dialogTag = "disconnectes_dialog"

    lateinit var viewModel:SharedViewModel

    val dataObserver= Observer<MutableList<NewsItem>>{
        view?.new_list_resycler_view?.visibility=View.VISIBLE
        val _adapter=view?.new_list_resycler_view?.adapter as AdapterNews
        _adapter.setmList(it)
     }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProviders.of(this).get(SharedViewModel::class.java)
       viewModel.newsMutableLiveData.observe(this,dataObserver)
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
        swiper?.setOnRefreshListener { loadAllNews() }
        loadAllNews()
    }



    override fun uploadClick(dialog: DialogNet) {
        dialog.dismiss()
        loadAllNews()
    }

    override fun cancelClick(dialog: DialogNet) {
        dialog.dismiss()
       // showComplete()
    }

    private fun loadAllNews() {
        view?.swipe_container?.isRefreshing = false
        view?.news_progress_bar?.visibility = View.GONE
        viewModel.getAllNews()

    }

    override fun newsClick(newsItem: NewsItem?) {
      /*  val fragment = FragmentFeedDetails()
        fragment.arguments= Bundle().apply {
            putString("http", newsItem?.link)
            putString("img",newsItem?.picture)
            Log.d("mylog","FragmentFeed - передать ссылку ${newsItem?.link}")
        }
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment, "detail_fragment")?.addToBackStack("detail_fragment")?.commit()
*/

    }
}