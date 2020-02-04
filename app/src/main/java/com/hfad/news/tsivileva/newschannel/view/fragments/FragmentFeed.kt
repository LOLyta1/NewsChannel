package com.hfad.news.tsivileva.newschannel.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.*
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.telecom.Connection
import android.telephony.AccessNetworkConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.adapter.AdapterNews
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsDecorator
import com.hfad.news.tsivileva.newschannel.presenter.HabrPresenter
import com.hfad.news.tsivileva.newschannel.presenter.ProgerPresenter
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.view.IView
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNet
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        IView,
        DialogNet.INetworkDialogListener {

    private var swiper: SwipeRefreshLayout? = null
    private val dialogTag = "disconnectes_dialog"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_load_progress_bar.visibility = View.VISIBLE

        view.new_list_resycler_view?.apply {
            adapter = AdapterNews()
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }
        swiper = view.swipe_container
        swiper?.setOnRefreshListener { loadAllNews() }
        loadAllNews()
    }


    override fun showNews(i: NewsItem?) {
        (view?.new_list_resycler_view?.adapter as AdapterNews).add(i)
    }

    override fun showError(er: Throwable) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager != null) {
            val dialog = DialogNet()
            dialog.setTargetFragment(this, 10)
            if (fragmentManager.findFragmentByTag(dialogTag) == null) {
                dialog.show(fragmentManager, dialogTag)
            }
        }
    }

    override fun showComplete() {
        view?.swipe_container?.isRefreshing = false
        view?.news_load_progress_bar?.visibility = View.GONE
    }

    override fun uploadClick(dialog: DialogNet) {
        dialog.dismiss()
        loadAllNews()
    }

    override fun cancelClick(dialog: DialogNet) {
        dialog.dismiss()
        showComplete()
    }

    private fun loadAllNews() {
        HabrPresenter(this@FragmentFeed).getNews(true)
        ProgerPresenter(this@FragmentFeed).getNews(true)
    }
}

