package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.news.tsivileva.newschannel.FEED_CONTENT_ERROR_DOWNLOADING
import com.hfad.news.tsivileva.newschannel.FEED_ERROR_DOWNLOADING
import com.hfad.news.tsivileva.newschannel.R
import kotlinx.android.synthetic.main.fragment_network_error.view.*

class FragmentNetworkError : Fragment() {

    interface IErrorFragmentListener {
        fun onFragmentErrorReloadButtonClick()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calledFragmentTag = parentFragmentManager.fragments.last().tag
        var listener: IErrorFragmentListener? = null

        when (calledFragmentTag) {
            FEED_ERROR_DOWNLOADING -> listener = parentFragment as FragmentFeeds
            FEED_CONTENT_ERROR_DOWNLOADING -> listener = parentFragment as FragmentFeedContent
        }

        view.network_reload_image_view.setOnClickListener {
            listener?.onFragmentErrorReloadButtonClick()
        }
    }
}