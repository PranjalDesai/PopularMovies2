package com.pranjaldesai.popularmovies2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pranjal on 8/11/17.
 */

public class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.PosterViewHolder> {

    final private GridItemClickListener mOnClickListener;
    private ArrayList<String> movieTitle, moviePosterString, movieID;
    private String tag;

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_grid_item;
        LayoutInflater inflater= LayoutInflater.from(context);

        View view= inflater.inflate(layoutID, parent, false);

        return new PosterViewHolder(view, context);
    }

    /*
    *   binds the view and the exact title and poster URL string
    */
    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.itemView.setTag(movieID.get(position));
        holder.bind(movieTitle.get(position), moviePosterString.get(position));
    }

    @Override
    public int getItemCount() {
        return movieTitle.size();
    }

    public interface GridItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdaptor(GridItemClickListener listener,
                        ArrayList<String>title, ArrayList<String>posterString,
                        ArrayList<String>id){
        mOnClickListener= listener;
        moviePosterString= posterString;
        movieTitle= title;
        movieID= id;
    }

    /*
    *   removes all the current data
    */
    public void removeData(){
        movieTitle=null;
        moviePosterString=null;
        movieID=null;
    }

    public void setCurrentTag(String tag){
        this.tag= tag;
    }

    public String getTag(){
        return tag;
    }

    /*
    *   updates the recyclerView
    */
    public void updateList(ArrayList<String> title, ArrayList<String> poster, ArrayList<String> id){
        movieTitle= new ArrayList<>();
        moviePosterString= new ArrayList<>();
        movieID= new ArrayList<>();
        movieTitle.addAll(title);
        movieID.addAll(id);
        moviePosterString.addAll(poster);
        notifyDataSetChanged();
    }


    class PosterViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterView;
        TextView titleView;
        Context mContext;

        public PosterViewHolder(View itemView, Context context){
            super(itemView);

            posterView= itemView.findViewById(R.id.ivPoster);
            titleView= itemView.findViewById(R.id.tv_movie_title);
            mContext= context;
            itemView.setOnClickListener(this);
        }

        /*
        *   binds the title and image to the recyclerview
        */
        void bind(String title, String posterUrl){
            titleView.setText(title);
            try {
                Picasso.with(mContext)
                        .load(posterUrl)
                        .placeholder(R.drawable.ic_menu_slideshow)
                        .into(posterView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /*
        *   Click Listener
        */
        @Override
        public void onClick(View view) {
            setCurrentTag((String)view.getTag());
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
