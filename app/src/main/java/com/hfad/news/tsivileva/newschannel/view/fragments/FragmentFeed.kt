package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.adapter.AdapterNews
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsDecorator
import com.hfad.news.tsivileva.newschannel.presenter.HabrPresenter
import com.hfad.news.tsivileva.newschannel.presenter.IPresenter
import com.hfad.news.tsivileva.newschannel.presenter.ProgerPresenter
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.IView
import com.hfad.news.tsivileva.newschannel.view.MainActivity
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() : Fragment(), IView {

    private var mContext: Context? = null

    private lateinit var mView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext=context
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_feed, container, false)

        mView.resycler_view?.apply {
            adapter = AdapterNews(mCardClick)
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(NewsDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }

        showLoading(true)
        
        HabrPresenter(this).getNews(true)
        ProgerPresenter(this).getNews(true)

        return mView
    }

    override fun showNews(i: NewsItem?) {
        (resycler_view?.adapter as AdapterNews).add(i)
    }

    override fun showError(er: Throwable) {
        Log.d(MainActivity.logname, "Ошибка" + er.message)
    }

    override fun showComplete() {
        showLoading(false)
    }


    val mCardClick = View.OnClickListener {
        Log.d("myLog", "click on recyclerView item" + it)
    }

    private fun showLoading(needShow:Boolean){
        if(needShow){
            resycler_view?.visibility = View.GONE
            progressBar?.visibility = View.VISIBLE
        }else{
            resycler_view?.visibility = View.VISIBLE
            progressBar?.visibility = View.GONE
        }
    }

}