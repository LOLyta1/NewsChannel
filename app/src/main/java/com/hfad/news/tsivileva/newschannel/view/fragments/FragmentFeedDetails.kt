package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
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
import com.hfad.news.tsivileva.newschannel.view_model.FeedDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails :
        Fragment(),
        DialogError.INetworkDialogListener,
        FragmentNetworkError.IErrorEventListener {
    private var contentUrl: String? = null
    private lateinit var viewModel: FeedDetailsViewModel

    private val loadingIsSuccessfullObserver = Observer<Boolean> { isSucess ->
        if (!isSucess) {
            showErrorDialog(childFragmentManager, this, "dialog_details_error")
            view?.news_content_progress_bar?.visibility=View.VISIBLE
        } else
            if (isSucess) {
                view?.news_content_progress_bar?.visibility=View.GONE
                removeErrorFragment(childFragmentManager, ERROR_FRAGMENT_FEED_DETAILS)
                viewModel.stopLoad()
                viewModel.cachedList.forEach {
                    Log.d("mylog"," В кэше - ${it.id}")
                }

            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentUrl = arguments?.getString("url")


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(FeedDetailsViewModel::class.java)
        viewModel.cachedNewsItemLiveData.observe(viewLifecycleOwner, Observer { showNews(it) })
        viewModel.loadingNewsStatus.observe(viewLifecycleOwner, loadingIsSuccessfullObserver)
        viewModel.loadeddNewsItemLiveData.observe(viewLifecycleOwner,Observer{showNews(it)})
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
        loadContent()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("mylog","onDetach()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("mylog","onDestroy()")
    }
    override fun onDestroyView() {
        super.onDestroyView()
       Log.d("mylog","onDestroyView()")
       /*viewModel.cachedNewsItemLiveData.removeObservers(this)
       viewModel.loadingNewsStatus.removeObservers(this)
      viewModel.stopLoad()*/
    }

    private fun showNews(newsItem: NewsItem?) {
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


    override fun errorDialogUploadClick(dialog: DialogError) {
        dialog.dismiss()
        loadContent()
    }

    override fun errorDialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        addErrorFragment(childFragmentManager,R.id.news_details_error_container, ERROR_FRAGMENT_FEED_DETAILS)
    }


    override fun onReloadButtonClick() {
        loadContent()
    }


    private fun loadContent() {
        contentUrl?.let {
            when (getSourceKind(it)) {
                Sources.PROGER -> viewModel.loadProgerContent(it)
                Sources.HABR -> viewModel.loadHabrContent(it)
            }
        }
    }



}