package com.hfad.news.tsivileva.newschannel.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.hfad.news.tsivileva.newschannel.*
import com.hfad.news.tsivileva.newschannel.activity.IPermissionListener
import com.hfad.news.tsivileva.newschannel.model.local.Content
import com.hfad.news.tsivileva.newschannel.model.local.Description
import com.hfad.news.tsivileva.newschannel.model.local.DescriptionAndFav
import com.hfad.news.tsivileva.newschannel.users_classes.DownloadingError
import com.hfad.news.tsivileva.newschannel.users_classes.DownloadingState
import com.hfad.news.tsivileva.newschannel.users_classes.DownloadingSuccessful
import com.hfad.news.tsivileva.newschannel.users_classes.DownloadNotification
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogNetworkError
import com.hfad.news.tsivileva.newschannel.view.dialogs.DialogSaveFile
import com.hfad.news.tsivileva.newschannel.view_model.FeedContentViewModel
import com.hfad.news.tsivileva.newschannel.view_model.ImageDownloading
import kotlinx.android.synthetic.main.fragment_feed_details.view.*


class FragmentFeedContent :
        Fragment(),
        DialogNetworkError.IDialogListener,
        IPermissionListener,
        DialogSaveFile.okClickListener {


    private var menu: Menu? = null
    private var descriptionAndFav: DescriptionAndFav? = DescriptionAndFav(Description())
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

        descriptionAndFav = arguments?.getParcelable("news_description")

        viewModel?.downloadContent(descriptionAndFav?.description?.link, descriptionAndFav?.description?.id)

        viewModel?.newsLiveData?.observe(viewLifecycleOwner, Observer { contentDownlodingResult: DownloadingState<Content>? ->

            view.news_content_progress_bar?.visibility = View.GONE

            when (contentDownlodingResult) {
                is DownloadingSuccessful -> {
                    view.feeds_details_error_container?.visibility = View.GONE
                    view.feed_content_container?.visibility = View.VISIBLE
                    showNews(contentDownlodingResult.data, descriptionAndFav)
                }
                is DownloadingError -> {
                    view.feeds_details_error_container?.visibility = View.VISIBLE
                    view.feed_content_container?.visibility = View.VISIBLE
                    showNews(contentDownlodingResult.cachedData, descriptionAndFav)
                    DialogNetworkError().show(childFragmentManager, DIALOG_WITH_ERROR)
                }
            }
        })

        view.news_details_link_text_view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(descriptionAndFav?.description?.link))
            val choosenIntent = Intent.createChooser(intent, "Choose application")
            startActivity(choosenIntent)
        }

        view.error_reload_button.setOnClickListener {
            view.news_content_progress_bar?.visibility = View.VISIBLE
            viewModel?.downloadContent(descriptionAndFav?.description?.link, descriptionAndFav?.description?.id)
        }

        view.download_image_button.setOnClickListener {
            val _activity = activity as Activity
            ActivityCompat.requestPermissions(_activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed_content_fragment, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (descriptionAndFav?.favorite?.isFav) {
            true -> menu.findItem(R.id.feed_content_add_to_favorites_menu_button)?.setIcon(R.drawable.heart_icon_full)
            false -> menu.findItem(R.id.feed_content_add_to_favorites_menu_button)?.setIcon(R.drawable.hear_empty_icon)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                parentFragmentManager.popBackStackImmediate()

            R.id.feed_content_add_to_favorites_menu_button -> {
                when (descriptionAndFav?.favorite?.isFav) {
                    true -> {
                        viewModel?.removeFromFavorite(descriptionAndFav?.description?.id)
                        menu?.findItem(R.id.feed_content_add_to_favorites_menu_button)?.setIcon(R.drawable.hear_empty_icon)
                    }
                    false -> {
                        viewModel?.addToFavorite(descriptionAndFav?.description?.id)
                        menu?.findItem(R.id.feed_content_add_to_favorites_menu_button)?.setIcon(R.drawable.heart_icon_full)
                    }
                }
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

    private fun showNews(content: Content, description: DescriptionAndFav?) {
        view?.let { _view: View ->
            _view.news_details_text_view?.text = content.contentText
            _view.news_details_date_text_view?.text = description?.description?.dateToString()
            _view.news_details_title_text_view?.text = description?.description?.title
            _view.news_details_link_text_view?.text = description?.description?.link

            val path = description?.description?.pictureLink
            if (path != null && path != "null") {
                _view.news_icon_card_view?.visibility = View.VISIBLE
                Glide.with(this)
                        .load(path)
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .fallback(R.drawable.no_photo)
                        .into(_view.news_details_image_view)
            } else {
                _view.news_icon_card_view?.visibility = View.GONE
            }
        }
    }


    override fun onDialogErrorReloadClick(dialogNetwork: DialogNetworkError) {
        viewModel?.downloadContent(descriptionAndFav?.description?.link, descriptionAndFav?.description?.id)
        view?.news_content_progress_bar?.visibility = View.VISIBLE
        dialogNetwork.dismiss()
    }

    override fun onDialogErrorCancelClick(dialogNetwork: DialogNetworkError) {
        dialogNetwork.dismiss()
        view?.news_content_progress_bar?.visibility = View.GONE
        view?.feeds_details_error_container?.visibility = View.VISIBLE
    }

    override fun getPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            val index = permissions.indexOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                DialogSaveFile().show(childFragmentManager, DialogSaveFile::class.java.canonicalName)
                Log.d(DEBUG_LOG, "пришло разрешение ! ")
            } else
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(context, "Отказано в доступе! Разрешите доступ в настройках приложения", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onSaveFileClick(fileName: String) {
        super.onSaveFileClick(fileName)
        val downloadNotification = DownloadNotification(context)
        viewModel?.downloadFile(descriptionAndFav?.description?.pictureLink)?.observe(viewLifecycleOwner, Observer { information: DownloadingState<Int>? ->
            when (information) {
                is DownloadingSuccessful -> {
                    if (information.data<100) {
                        downloadNotification.update(information.data,Environment.getExternalStorageDirectory().path)
                    }else{
                        downloadNotification.hideProgress(resources.getString(R.string.downloading_file_complete))

                    }
                }
                is DownloadingError -> {
                    DownloadNotification(context).update(0, "Ошибка загрузки!")
                }
            }

        })
    }
}












