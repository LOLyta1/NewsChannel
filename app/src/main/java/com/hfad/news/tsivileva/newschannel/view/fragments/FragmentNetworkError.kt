package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.news.tsivileva.newschannel.*
import kotlinx.android.synthetic.main.fragment_network_error.view.*

class FragmentNetworkError : Fragment() {


    interface IErrorEventListener {
        fun onReloadButtonClick()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calledFragmentTag = parentFragmentManager.fragments.last().tag
        var listener: IErrorEventListener?=null
        when (calledFragmentTag) {
            ERROR_FRAGMENT_FEED_DETAILS -> listener = parentFragment as FragmentFeedDetails
            ERROR_FRAGMENT_FEED->listener= parentFragment as FragmentFeed
        }

        view.network_reload_image_view.setOnClickListener {
            listener?.onReloadButtonClick()
        }
    }


}