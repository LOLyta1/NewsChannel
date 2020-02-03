package com.hfad.news.tsivileva.newschannel.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.news.tsivileva.newschannel.R;
import com.hfad.news.tsivileva.newschannel.adapter.items.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Адаптер для RecyclerView
 */
public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    /*список элементов*/
    private List<NewsItem> mList;

    private  View.OnClickListener mItemClickListener;

    public AdapterNews(View.OnClickListener listener) {
        this.mList = new ArrayList<NewsItem>();
        this.mItemClickListener=listener;
    }

    @NonNull
    @Override
    /*для каждого элемента списка "раздувает" представление при помощи разметки*/
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cv = (View) LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list, parent, false);

     /*   if (mItemClickListener != null)
            cv.setOnClickListener(mItemClickListener);*/

        return new ViewHolder(cv);
    }

    /*вложенный класс-указывает, что за элемент будет использоваться в качестве предсталения, хранит сылку
     * на этот элемент*/
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View card;
        private TextView titleTextView;
        private ImageView imageView;
        private TextView linkView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.card = itemView;
            this.titleTextView = card.findViewById(R.id.titleTextView);
            this.imageView = card.findViewById(R.id.pictureImageView);
            this.linkView = card.findViewById(R.id.linkTextView);
        }


    }

    @Override
    /*связывает представление и данные из спика*/
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (this.mList != null) {
            String title = mList.get(position).getTitle();
            String link = mList.get(position).getLink();
            String picture = mList.get(position).getPicture();

            setValidText(holder.titleTextView, title, "Нет текста:");
            setValidText(holder.linkView, link, "Нет ссылки");

            if (picture != null) {
                Picasso.get().load(picture)
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(holder.imageView);
            } else {
                Picasso.get().load("https://images.app.goo.gl/njSMVbDb6qBv71rD6")
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(holder.imageView);
            }
        }
    }

    /*получить размер списка элементов*/

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /*добавить элемент в список*/

    public void add(NewsItem newsItem) {
        this.mList.add(newsItem);
        notifyDataSetChanged();

    }

    public void cleare(){
       this.mList.clear();
      notifyDataSetChanged();
    }
    private void setValidText(View v, String text, String substitutionText) {
        if (text != null) {
            ((TextView) v).setText(text);
        } else {
            ((TextView) v).setText(substitutionText);
        }
    }

    public void setmItemClickListener(View.OnClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setmList(List<NewsItem> mList) {
        this.mList = mList;
    }
}
