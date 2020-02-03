package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Context
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.adapter.AdapterNews
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsDecorator
import com.hfad.news.tsivileva.newschannel.presenter.HabrPresenter
import com.hfad.news.tsivileva.newschannel.presenter.ProgerPresenter
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.IView
import com.hfad.news.tsivileva.newschannel.view.MainActivity
import com.hfad.news.tsivileva.newschannel.view.dialogs.DisconnectedDialog
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() : Fragment(), IView {
    private  var mConnectionManager:ConnectivityManager?=null

    private lateinit var mContext: Context
    private lateinit var mView: View
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var mIView: IView
    val request = NetworkRequest.Builder().build()

    private var mConnectionCallbacks=object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                DisconnectedDialog(mIView).show(fragmentManager, "disconnectes_dialog")
            } else {
                //nothing to do
            }
        }
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            reloadNews(mIView)
        }
    }



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

        mConnectionManager=activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mConnectionManager?.registerNetworkCallback(request,mConnectionCallbacks)

        mView.progressBar.visibility = View.VISIBLE
        mIView = this

        mSwipeRefreshLayout = mView.swipeContainer
        mSwipeRefreshLayout?.setOnRefreshListener { reloadNews(mIView) }
        reloadNews(mIView)

        return mView
    }

    override fun reloadNews(iView: IView) {
        if(mConnectionManager?.activeNetwork!=null){
            HabrPresenter(iView).getNews(true)
            ProgerPresenter(iView).getNews(true)
        }else{
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                DisconnectedDialog(iView).show(fragmentManager, "disconnectes_dialog")
            } else {
                //nothing to do
            }
        }
    }



    override fun showNews(i: NewsItem?) {
        Log.d(MainActivity.logname, "показать новости  - showNews()")
        (mView.resycler_view.adapter as AdapterNews).add(i)
    }

    override fun showError(er: Throwable) {
        Toast.makeText(mContext,"Произошла ошибка при загрузке! ${er.message}",Toast.LENGTH_SHORT).show()

    }

    override fun showComplete() {
        mView.swipeContainer?.isRefreshing = false
        mView.progressBar.visibility = View.GONE
    }

    val mCardClick = View.OnClickListener {
        Log.d("myLog", "click on recyclerView item" + it)
    }

    override fun onDestroy() {
        super.onDestroy()
        mConnectionManager?.unregisterNetworkCallback(mConnectionCallbacks)
    }
}