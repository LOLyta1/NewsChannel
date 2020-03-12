package com.hfad.news.tsivileva.newschannel.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hfad.news.tsivileva.newschannel.R
import com.hfad.news.tsivileva.newschannel.model.local.NewsAndFav

/*
 * Адаптер для RecyclerView
 */
class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    interface INewsItemClickListener {
        fun onNewsClick(position: Int, view: View)
    }

    var listener: INewsItemClickListener? = null
    var list = listOf<NewsAndFav>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(cv, listener)
    }

    class ViewHolder(card: View, val clickListener: INewsItemClickListener?) :
            RecyclerView.ViewHolder(card),
            View.OnClickListener {
        val titleTextView = card.findViewById<TextView>(R.id.news_title_text_view)
        val imageView = card.findViewById<ImageView>(R.id.news_image_view)
        val linkView = card.findViewById<TextView>(R.id.news_link_text_view)
        val dateView = card.findViewById<TextView>(R.id.news_pub_date_text_view)
        val favImageView = card.findViewById<ImageView>(R.id.add_to_fav_image_view)

        init {
            imageView.setOnClickListener(this)
            favImageView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener!!.onNewsClick(adapterPosition, view)
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = list[position].newsInfo.title
        holder.linkView.text = list[position].newsInfo.link
        holder.dateView.text = list[position].newsInfo.dateToString()

        if (list[position].newsInfo.pictureSrc != "") {
            Glide.with(holder.imageView)
                    .load(list[position].newsInfo.pictureSrc)
                    .centerCrop()
                    .placeholder(R.drawable.no_photo)
                    .error(R.drawable.no_photo)
                    .fallback(R.drawable.no_photo)
                    .into(holder.imageView)

        }
        list[position].newsFav?.isFav?.let {
            if (it) {
                holder.favImageView.setImageResource(R.drawable.heart_icon_full)
            } else {
                holder.favImageView.setImageResource(R.drawable.hear_empty_icon)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.favImageView.setImageResource(R.drawable.hear_empty_icon)
    }

    override fun getItemCount(): Int = list.count()

}