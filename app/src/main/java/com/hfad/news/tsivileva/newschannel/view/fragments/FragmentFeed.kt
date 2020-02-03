package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    private lateinit var mContext: Context
    private lateinit var mView: View
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var mIView: IView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_feed, container, false)
        mView.resycler_view?.apply {
            adapter = AdapterNews(mCardClick)
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(NewsDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }

        mView.progressBar.visibility = View.VISIBLE
        mIView = this
        getAllNews(mIView)
        mSwipeRefreshLayout = mView.swipeContainer
        mSwipeRefreshLayout?.setOnRefreshListener { getAllNews(mIView) }

        return mView
    }

    private fun getAllNews(iView: IView) {
        HabrPresenter(iView).getNews(true)
        ProgerPresenter(iView).getNews(true)
    }

    override fun showNews(i: NewsItem?) {
        Log.d(MainActivity.logname, "показать новости  - showNews()")
        (mView.resycler_view.adapter as AdapterNews).add(i)
    }

    override fun showError(er: Throwable) {
        Log.d(MainActivity.logname, "Ошибка" + er.message)
    }

    override fun showComplete() {
        swipeContainer?.isRefreshing = false
        mView.progressBar.visibility = View.GONE
    }

    val mCardClick = View.OnClickListener {
        Log.d("myLog", "click on recyclerView item" + it)
    }
}