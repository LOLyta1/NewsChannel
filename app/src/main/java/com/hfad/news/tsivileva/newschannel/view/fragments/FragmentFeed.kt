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
import androidx.fragment.app.FragmentTransaction
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
import com.hfad.news.tsivileva.newschannel.view.dialogs.INetworkDialogListener
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() : Fragment(), IView,INetworkDialogListener {

    private  var mConnectionManager:ConnectivityManager?=null
    private lateinit var mContext: Context
    private lateinit var mView: View
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var disconnectedDialog= DisconnectedDialog()
    private lateinit var mIView: IView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }
    override fun uploadClick() {
        disconnectedDialog.dismiss()
        reloadNews()
    }

    override fun cancelClick() {
        disconnectedDialog.dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_feed, container, false)
        mView.resycler_view?.apply {
            adapter = AdapterNews(mCardClick)
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(NewsDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }
        mConnectionManager=activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mView.progressBar.visibility = View.VISIBLE
        mIView = this

        mSwipeRefreshLayout = mView.swipeContainer
        mSwipeRefreshLayout?.setOnRefreshListener { reloadNews() }
        reloadNews()

        return mView
    }

    fun reloadNews() {
        if(mConnectionManager?.activeNetwork!=null){
            HabrPresenter(mIView).getNews(true)
            ProgerPresenter(mIView).getNews(true)
        }else{
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                disconnectedDialog.setTargetFragment(this, 10)
                disconnectedDialog.show(fragmentManager,"disconnectes_dialog")
//                disconnectedDialog.setListener(this)
            } else {
                //nothing to do
            }
        }
    }

    override fun showNews(i: NewsItem?) {
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
}