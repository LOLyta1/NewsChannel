package com.hfad.news.tsivileva.newschannel.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;

import com.hfad.news.tsivileva.newschannel.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Адаптер для RecyclerView
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private List<NewsItem> mList;
    private INewsItemClickListener listener;

    public interface INewsItemClickListener {
        void onNewsClick(Integer position);
    }

    public void setmList(List<NewsItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
    public NewsListAdapter() {
        this.mList = new ArrayList<>();
    }

    public void setListener(INewsItemClickListener listener){
        this.listener=listener;
    }

    public List<NewsItem> getList(){
        return this.mList;
    }

    @NonNull
    @Override
    /*для каждого элемента списка "раздувает" представление при помощи разметки*/
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cv = LayoutInflater. from(parent.getContext()). inflate(R.layout.item_list, parent, false);
        return new ViewHolder(cv,listener);
    }

    /*вложенный класс-указывает, что за элемент будет использоваться в качестве предсталения, хранит сылку
     * на этот элемент*/
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View card;
        private TextView titleTextView;
        private ImageView imageView;
        private TextView linkView;
        private TextView dateView;
        private INewsItemClickListener clickListener;

        public ViewHolder(@NonNull View itemView, INewsItemClickListener clickListener) {
            super(itemView);
            this.card = itemView;
            this.titleTextView = card.findViewById(R.id.news_title_text_view);
            this.imageView = card.findViewById(R.id.news_image_view);
            this.linkView = card.findViewById(R.id.news_link_text_view);
            this.dateView=card.findViewById(R.id.news_pub_date_text_view);
            this.clickListener=clickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onNewsClick(getAdapterPosition());//getAdapterPosition());
        }
    }

    @Override
    /*связывает представление и данные из спика*/
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (this.mList != null) {
            String title = mList.get(position).getTitle();
            String link = mList.get(position).getLink();
            String picture = mList.get(position).getPicture();
            String date= mList.get(position).getStringDate();

            holder.titleTextView.setText(title);
            holder.linkView.setText(link);
            holder.dateView.setText(date);

            if (picture != null) {
                Picasso.get().load(picture)
                        .placeholder(R.drawable.no_photo)
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .error(R.drawable.no_photo)
                        .into(holder.imageView);
            } else {
                Picasso.get().load("https://images.app.goo.gl/njSMVbDb6qBv71rD6")
                        .placeholder(R.drawable.no_photo)
                        .memoryPolicy(MemoryPolicy.NO_STORE)
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

    public void scrollToBottom(){

    }


    private void setValidText(View v, String text, String substitutionText) {
        if (text != null) {
            ((TextView) v).setText(text);
        } else {
            ((TextView) v).setText(substitutionText);
        }
    }

}