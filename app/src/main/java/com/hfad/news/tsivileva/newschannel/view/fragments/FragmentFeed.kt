package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsListAdapter
import com.hfad.news.tsivileva.newschannel.adapter.NewsListDecorator
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FragmentFeed() :
        Fragment(),
        DialogError.INetworkDialogListener,
        NewsListAdapter.IClickListener {

    private var swiper: SwipeRefreshLayout? = null
    lateinit var viewModel: NewsViewModel

    val newsListObserver = Observer<MutableList<NewsItem>> {
        getNewsRecyclerAdapter().setmList(it)
    }

    val loadingObserver = Observer<Boolean> { success ->
        if (success) {
            loadingBar(hiden = true)
            Toast.makeText(context, resources.getText(R.string.load_is_successful_text), Toast.LENGTH_LONG).show()
        } else {
            DialogError().apply {
                setTargetFragment(this@FragmentFeed, 10)
                show(getManager(), "dialog_error")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getNewsViewModel()
        viewModel.newsListLiveData.observe(this, newsListObserver)
        viewModel.loadSuccessfulLiveData.observe(this, loadingObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingBar(hiden = false)
        view.news_resycler_view?.apply {
            adapter = NewsListAdapter(this@FragmentFeed)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(NewsListDecorator(left = 10, top = 10, right = 10, bottom = 10))
        }
        swiper = view.swipe_container
        swiper?.setOnRefreshListener { viewModel.loadAllNews() }
        viewModel.loadAllNews()
    }

    override fun dialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
        viewModel.loadAllNews()
    }

    override fun dialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
    }


    override fun newsClick(position: Int) {
        val fragment = FragmentFeedDetails()
        fragment.arguments = Bundle().apply {
            putInt("index", position)
        }
      //  activity?.supportFragmentManager?.beginTransaction()?.hide(this)?.add(R.id.container, fragment, "detail_fragment")?.addToBackStack("detail_fragment")?.commit()
    }
}

