package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        DialogError.INetworkDialogListener,
        NewsListAdapter.IClickListener {


    lateinit var viewModel: NewsViewModel

    val loadingStatusObserver = Observer<Boolean> { success ->
        if (!success) showErrorDialog(activity, this, "dialog_feed_error")
        view?.swipe_container?.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(activity)
        viewModel.newsList.observe(this, Observer {  getRecyclerAdapter().setmList(it) })
        viewModel.loadStatus.observe(this, loadingStatusObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.news_resycler_view?.apply {
            adapter = NewsListAdapter(this@FragmentFeed)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }
       view.swipe_container?.setOnRefreshListener {
            viewModel.newsList.value?.clear()
            getRecyclerAdapter().setmList(viewModel.newsList.value)
            loadNews()
        }
        loadNews()
    }

    private fun loadNews(){
        val newsList=viewModel.newsList.value
        if(newsList!=null && newsList.count()>0){
            getRecyclerAdapter().setmList(newsList)
        }else{
            view?.swipe_container?.isRefreshing = true
            viewModel.loadAllNews()
        }
    }

    override fun dialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
       loadNews()
    }

    override fun dialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        view?.swipe_container?.isRefreshing = false
    }

    override fun newsClick(url: String) {
        val fragment = FragmentFeedDetails()
        fragment.arguments = Bundle().apply {
            putString("url", url)
        }
        activity?.supportFragmentManager?.
                beginTransaction()?.
                replace(R.id.container, fragment, "detail_fragment")?.
                addToBackStack("detail_fragment")?.
                commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopLoad()
    }

}

