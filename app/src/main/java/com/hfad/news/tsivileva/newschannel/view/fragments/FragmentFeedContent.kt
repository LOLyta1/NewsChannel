package com.hfad.news.tsivileva.newschannel.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav
import com.hfad.news.tsivileva.newschannel.model.local.NewsContent
import com.hfad.news.tsivileva.newschannel.model.local.NewsDescription
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view_model.FeedContentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedContent :
        Fragment(),
        DialogNetworkError.IDialogListener {

    private var newsDescription: NewsAndFav? = NewsAndFav()
    var viewModel: FeedContentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.back_icon)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(FeedContentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_feed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsDescription = arguments?.getParcelable("news_description")

        viewModel?.downloadContent(newsDescription?.newsInfo?.link, newsDescription?.newsInfo?.id)

        viewModel?.newsLiveData?.observe(viewLifecycleOwner, Observer { contentDownlodingResult: DownloadingState<NewsContent>? ->
            when (contentDownlodingResult) {
                is DownloadingSuccessful -> {
                    view.news_content_progress_bar?.visibility = View.GONE
                    view.feeds_details_error_container?.visibility = View.GONE
                    view.feed_content_container?.visibility = View.VISIBLE
                    showNews(contentDownlodingResult.data, newsDescription)
                }
                is DownloadingError -> {
                    view.news_content_progress_bar?.visibility = View.GONE
                    view.feeds_details_error_container?.visibility = View.VISIBLE
                    view.feed_content_container?.visibility = View.VISIBLE
                    showNews(contentDownlodingResult.cachedData, newsDescription)
                }
            }
        })

        view.news_details_link_text_view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsDescription?.newsInfo?.link))
            val choosenIntent = Intent.createChooser(intent, "Choose application")
            startActivity(choosenIntent)
        }

        view.error_reload_button.setOnClickListener {
            viewModel?.downloadContent(newsDescription?.newsInfo?.link, newsDescription?.newsInfo?.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed_content_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> parentFragmentManager.popBackStackImmediate()
            R.id.feed_content_add_to_favorites_menu_button -> {
                //TODO() сделать сохранение в базу
            }
            R.id.feed_content_share_menu_button -> {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getFeedInfo())
                    type = "text/plain"
                }
                val chooserIntent = Intent.createChooser(intent, "Select source")
                startActivity(chooserIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNews(newsContent: NewsContent, description: NewsAndFav?) {
        view?.news_details_text_view?.text = newsContent.content
        view?.news_details_date_text_view?.text = description?.newsInfo?.dateToString()
        view?.news_details_title_text_view?.text = description?.newsInfo?.title
        view?.news_details_link_text_view?.text = description?.newsInfo?.link

        val path = description?.newsInfo?.picture
        if (path != null && path.isNotEmpty()) {
            view?.new_details_car_view?.visibility = View.VISIBLE
            Picasso.get().load(path).placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .into(view?.news_details_image_view);
        } else {
            view?.new_details_car_view?.visibility = View.GONE
        }
    }

    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        view?.news_content_progress_bar?.visibility = View.VISIBLE
        dialogNetwork.dismiss()
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.news_content_progress_bar?.visibility = View.GONE
        view?.feeds_details_error_container?.visibility = View.VISIBLE
    }
}


