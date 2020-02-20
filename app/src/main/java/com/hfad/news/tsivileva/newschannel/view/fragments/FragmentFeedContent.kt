package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Intent
import android.net.Uri
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

class FragmentFeedContent :
        Fragment(),
        DialogError.IDialogListener,
        FragmentNetworkError.IErrorFragmentListener {

    private var contentUrl: String? = null
    private lateinit var viewModel: FeedDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentUrl = arguments?.getString("url")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(FeedDetailsViewModel::class.java)
        viewModel.news.observe(viewLifecycleOwner, Observer { showNews(it) })
        viewModel.isDownloadSuccessful.observe(viewLifecycleOwner, Observer{ isSuccessful ->
            if (isSuccessful) {
                removeFragmentError(childFragmentManager, FRAGMENT_WITH_ERROR_DOWNLOADING_FEED_CONTENT)
            } else {
                view?.news_content_progress_bar?.visibility = View.VISIBLE
                DialogError().apply{isCancelable=false}. show(childFragmentManager, DIALOG_FRAGMENT_WITH_ERROR)
            }
            viewModel.stopLoad()
        })

        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.news_details_link_text_view.setOnClickListener {
                val intent= Intent(Intent.ACTION_VIEW, Uri.parse(contentUrl))
                val choosenIntent=Intent.createChooser(intent,"Choose application")
                startActivity(choosenIntent)
        }

        loadContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //искуственно обнуляем новость, для того, чтобы после нажатия "back" и открытия другой новости обновить UI
        viewModel.news.postValue(NewsItem())
    }

    private fun showNews(newsItem: NewsItem?) {
        if (newsItem?.id!= null) {
            view?.news_content_container?.visibility = View.VISIBLE
            view?.news_content_progress_bar?.visibility = View.GONE

            view?.news_details_text_view?.text = newsItem.content
            view?.news_details_date_text_view?.text = newsItem.date
            view?.news_details_title_text_view?.text = newsItem.title
            view?.news_details_link_text_view?.text = newsItem.link

            val path = newsItem.picture
            if (path != null && !path.isEmpty()) {
                view?.new_details_car_view?.visibility=View.VISIBLE
                Picasso.get().load(path).placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(view?.news_details_image_view);
            }else{
                view?.new_details_car_view?.visibility = View.GONE
            }
        }
    }

    override fun onDialogReloadClick(dialog: DialogError) {
        dialog.dismiss()
        loadContent()
    }

    override fun onDialogCancelClick(dialog: DialogError) {
        dialog.dismiss()
        view?.news_content_progress_bar?.visibility=View.GONE
        showErrorFragment(
                fragmentManager = childFragmentManager,
                containerId = R.id.news_details_error_container,
                tag = FRAGMENT_WITH_ERROR_DOWNLOADING_FEED_CONTENT
        )
    }

    override fun onFragmentErrorReloadButtonClick() {
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

    private fun getSourceKind(link:String?) : Sources?{
        link?.let {
            if( it.contains("habr.com")) {
                return Sources.HABR
            }else
                if( it.contains("tproger.ru")) {
                    return Sources.PROGER
                }
        }
        return null
    }
}