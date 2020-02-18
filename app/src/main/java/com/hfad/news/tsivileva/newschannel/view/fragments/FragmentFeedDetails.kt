package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.adapter.Sources
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogError
import com.hfad.news.tsivileva.newschannel.view_model.NewsContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails : Fragment(), DialogError.INetworkDialogListener {
    private var contentUrl: String? = null
    private lateinit var viewModel: NewsContentViewModel

    private val loadingStatusObserver = Observer<Boolean> { isSucess ->
        if (!isSucess) {
            showErrorDialog(activity, this, "dialog_details_error")
            view?.network_error_frame_layout?.visibility=View.VISIBLE
                 }else
            if(isSucess){
                view?.network_error_frame_layout?.visibility=View.GONE
                viewModel.stopLoad()
           //     view?.news_details_swipe?.isRefreshing=false
                //logStateSwiper(view?.news_details_swipe,"FragmentFeedDetails.loadingStatusObserver()")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentUrl = arguments?.getString("url")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel =ViewModelProviders.of(this).get(NewsContentViewModel::class.java)
        viewModel.getNewsContentLiveData().observe(viewLifecycleOwner, Observer { showNews(it) })
        viewModel.getLoadingNewsStatus().observe(viewLifecycleOwner, loadingStatusObserver)
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCheckingCash()
      /*  view.news_details_swipe.setOnRefreshListener {
            when (getSourceKind(contentUrl)) {
                Sources.Proger -> viewModel.loadProgerContent(contentUrl.toNonNullString())
                Sources.HABR -> viewModel.loadHabrContent(contentUrl.toNonNullString())
            }
        }*/
    }

    private fun loadCheckingCash() {
      //  view?.news_details_swipe?.isRefreshing=true
      //  logStateSwiper(view?.news_details_swipe,"FragmentFeedDetails.loadCheckingCash()")
        val newsInCached = findNewsInCacheByLink(viewModel.getCachedList(), contentUrl.toNonNullString())
        if (newsInCached == null) {
            viewModel.cleareNewsContent()
            when (getSourceKind(contentUrl)) {
                Sources.Proger -> viewModel.loadProgerContent(contentUrl.toNonNullString())
                Sources.HABR -> viewModel.loadHabrContent(contentUrl.toNonNullString())
            }
        } else {
            viewModel.setNewsContent(newsInCached)
        }
    }

    fun showNews(newsItem: NewsItem?) {
       // view?.news_details_swipe?.isRefreshing = false
        view?.news_details_text_view?.text = newsItem?.content
        view?.news_details_date_text_view?.text = newsItem?.date
        view?.news_details_title_text_view?.text = newsItem?.title
        view?.news_details_link_text_view?.text = newsItem?.link

        val path = newsItem?.picture
        if (path != null && !path.isEmpty()) {
            view?.new_details_car_view?.visibility = View.VISIBLE
            Picasso.get().load(path).placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .into(view?.news_details_image_view);
        } else {
            view?.new_details_car_view?.visibility = View.GONE
        }
    }


    override fun dialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
        when (getSourceKind(contentUrl)) {
            Sources.Proger -> viewModel.loadProgerContent(contentUrl.toNonNullString())
            Sources.HABR -> viewModel.loadHabrContent(contentUrl.toNonNullString())
        }
    }

    override fun dialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.getNewsContentLiveData().removeObservers(this)
        viewModel.getLoadingNewsStatus().removeObservers(this)
    }
}