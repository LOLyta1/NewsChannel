package com.hfad.news.tsivileva.newschannel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.news.tsivileva.newschannel.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/*
 * Адаптер для RecyclerView
 */
class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    interface INewsItemClickListener {
        fun onNewsClick(position: Int?)
    }

    var list = listOf<NewsItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: INewsItemClickListener? = null

    override fun getItemCount(): Int = list.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(cv, listener)
    }

    class ViewHolder(card: View, val clickListener: INewsItemClickListener?) : RecyclerView.ViewHolder(card), View.OnClickListener {
        val titleTextView = card.findViewById<TextView>(R.id.news_title_text_view)
        val imageView = card.findViewById<ImageView>(R.id.news_image_view)
        val linkView = card.findViewById<TextView>(R.id.news_link_text_view)
        val dateView = card.findViewById<TextView>(R.id.news_pub_date_text_view)

        init {
            card.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener!!.onNewsClick(adapterPosition)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = list[position].title
        holder.linkView.text = list[position].link
        holder.dateView.text = list[position].dateToString()

        if (list[position].picture != null) {
            Picasso.get().load(list[position].picture)
                    .placeholder(R.drawable.no_photo)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .error(R.drawable.no_photo)
                    .into(holder.imageView)
        } else {
            Picasso.get().load("https://images.app.goo.gl/njSMVbDb6qBv71rD6")
                    .placeholder(R.drawable.no_photo)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .error(R.drawable.no_photo)
                    .into(holder.imageView)
        }
    }
}