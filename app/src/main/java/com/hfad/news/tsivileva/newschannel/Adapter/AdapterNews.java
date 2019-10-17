package com.hfad.news.tsivileva.newschannel.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.news.tsivileva.newschannel.Model.implementation.Habr.Habr;
import com.hfad.news.tsivileva.newschannel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
* Адаптер для RecyclerView
*/
public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    /*список элементов*/
    private List<ItemAdapter> list;

    public AdapterNews() {
        this.list = new ArrayList<ItemAdapter>();

    }
    public AdapterNews(List<ItemAdapter> list) {
        this.list = list;

    }


    @NonNull
    @Override
    /*для каждого элемента списка "раздувает" представление при помощи разметки*/
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CardView cv=(CardView) LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list,parent,false);

        return new ViewHolder(cv);
    }

    /*вложенный класс-указывает, что за элемент будет использоваться в качестве предсталения, хранит сылку
    * на этот элемент*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
                private CardView card;

                public ViewHolder(@NonNull CardView itemView) {
                    super(itemView);
                    this.card=itemView;
                }
    }


    @Override
    /*связывает представление и данные из спика*/
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    /*Обработчик нажатия на n-ю карточку*/
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView summTV=(TextView) v.findViewById(R.id.summaryTextView);

                if(summTV.getVisibility()==View.GONE){
                    summTV.setVisibility(View.VISIBLE);
                }else {
                    summTV.setVisibility(View.GONE);
                }
            }
        });
        /*заполнить карточку данными*/
        fillData(holder.card,position);



  }
    /*получить размер списка элементов*/
    @Override
    public int getItemCount() {
      return list.size();
    }


/*добавить элемент в список*/
    public void add(ItemAdapter item){
        this.list.add(item);
        notifyDataSetChanged();

    }


    /*
    * Медот заливки данных
    * */
    private  void fillData(CardView cardView,int position){

        if(this.list!=null){

            TextView titleView=(TextView) cardView.findViewById(R.id.titleTextView);
            TextView descriptionView=(TextView) cardView.findViewById(R.id.summaryTextView);
            ImageView imageView=(ImageView) cardView.findViewById(R.id.pictureImageView);
            TextView dateView=(TextView)cardView.findViewById(R.id.dateTextView);
            TextView linkView=(TextView)cardView.findViewById(R.id.linkTextView);

            String title=list.get(position).getTitle();
            String desc=list.get(position).getSummarry();
            String date=list.get(position).getDate();
            String link=list.get(position).getLink();
            String picture=list.get(position).getPicture();


            if (title!=null) {
                titleView.setText(title);
            } else{
                titleView.setText("Нет текста:");
            };

            if (desc!=null) {
                descriptionView.setText(desc);
            } else{
                titleView.setText("Нет описания");
            };

            if(date!=null){
                dateView.setText(date);
            }else{
                dateView.setText("Нет описания");
            }

            if(link!=null){
                linkView.setText(link);
            }else{
                linkView.setText("Нет ссылки");
            }

            if(picture!=null) {
                Picasso.get().load(picture)
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(imageView);
            }else {
                Picasso.get().load("https://images.app.goo.gl/njSMVbDb6qBv71rD6")
                        .placeholder(R.drawable.no_photo)
                        .error(R.drawable.no_photo)
                        .into(imageView);
            }

        }

    }
}
