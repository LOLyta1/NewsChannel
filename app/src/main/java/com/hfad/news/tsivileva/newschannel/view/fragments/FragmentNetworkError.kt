package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.news.tsivileva.newschannel.*
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
        var listener: IErrorFragmentListener?=null
        when (calledFragmentTag) {
            FRAGMENT_WITH_ERROR_DOWNLOADING_FEED->listener= parentFragment as FragmentFeed
            FRAGMENT_WITH_ERROR_DOWNLOADING_FEED_CONTENT -> listener = parentFragment as FragmentFeedContent
        }

        view.network_reload_image_view.setOnClickListener {
            listener?.onFragmentErrorReloadButtonClick()
        }
    }


}