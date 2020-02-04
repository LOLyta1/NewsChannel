package com.hfad.news.tsivileva.newschannel.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_details.view.*

class FragmentFeedDetails:Fragment() {
    var news:NewsItem?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_feed_details,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            view.news_details_date_text_view.text=news?.date
            Picasso.get().load(news?.picture).placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .into(view.news_details_image_view);
            view.news_details_link_text_view.text=news?.link
            view.news_details_text_view.text=news?.summarry

        }
}