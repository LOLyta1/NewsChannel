package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.DIALOG_WITH_ERROR
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.adapter.NewsItem
import com.hfad.news.tsivileva.newschannel.getFeedsContentSource
import com.hfad.news.tsivileva.newschannel.repository.DownloadedFeed
import com.hfad.news.tsivileva.newschannel.repository.DownloadingError
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view_model.FeedContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedContent :
        Fragment(),
        DialogNetworkError.IDialogListener {

    private var contentUrl: String = ""
    private lateinit var viewModel: FeedContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        contentUrl = arguments?.getString("url").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(FeedContentViewModel::class.java)


        viewModel.downloading.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is DownloadedFeed -> {
                    if (!status.feed.isEmpty()) {
                        view?.news_content_progress_bar?.visibility = View.GONE
                        view?.feeds_details_error_container?.visibility = View.GONE
                        view?.feed_content_container?.visibility = View.VISIBLE
                        showNews(status.feed)
                    }
                }
                is DownloadingError -> {
                    view?.news_content_progress_bar?.visibility = View.VISIBLE
                    view?.feeds_details_error_container?.visibility = View.VISIBLE
                    DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                }
            }
        })
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.news_details_link_text_view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contentUrl))
            val choosenIntent = Intent.createChooser(intent, "Choose application")
            startActivity(choosenIntent)
        }
        view.error_reload_button.setOnClickListener {
            viewModel.loadContent(contentUrl, getFeedsContentSource(contentUrl))
        }

        view.news_content_progress_bar?.visibility = View.VISIBLE
        viewModel.loadContent(contentUrl, getFeedsContentSource(contentUrl))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.refreshData()
    }

    private fun showNews(newsItem: NewsItem) {
        if (!newsItem.isEmpty()) {
            view?.news_details_text_view?.text = newsItem.content
            view?.news_details_date_text_view?.text = newsItem.dateToString()
            view?.news_details_title_text_view?.text = newsItem.title
            view?.news_details_link_text_view?.text = newsItem.link

            val path = newsItem.picture
            if (path != "" && path.isNotEmpty()) {
                view?.new_details_car_view?.visibility = View.VISIBLE
                Picasso.get().load(path).placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(view?.news_details_image_view);
            } else {
                view?.new_details_car_view?.visibility = View.GONE
            }
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        view?.news_content_progress_bar?.visibility = View.VISIBLE
        dialogNetwork.dismiss()
        viewModel.loadContent(contentUrl, getFeedsContentSource(contentUrl))
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.news_content_progress_bar?.visibility = View.GONE
        view?.feeds_details_error_container?.visibility = View.VISIBLE
    }
}


