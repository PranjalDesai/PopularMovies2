package com.pranjaldesai.popularmovies2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pranjaldesai.popularmovies2.apiData.VideosResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pranjal on 9/20/17.
 */

public class TrailerAdaptor extends RecyclerView.Adapter<TrailerAdaptor.ThumbnailViewHolder> {
    final private LinearItemClickListener mOnClickListener;
    private ArrayList<VideosResult> trailers;
    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.trailer_grid_item;
        LayoutInflater inflater= LayoutInflater.from(context);

        View view= inflater.inflate(layoutID, parent, false);

        return new ThumbnailViewHolder(view, context);
    }

    /*
    *   binds the view and the exact trailer URL string
    */
    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        holder.bind(trailers.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface LinearItemClickListener {
        void onListTrailerClick(int clickedItemIndex);
    }

    public TrailerAdaptor(LinearItemClickListener listener,
                        ArrayList<VideosResult>results){
        mOnClickListener= listener;
        trailers= results;
    }

    /*
    *   removes all the current data
    */
    public void removeData(){
        trailers=null;
    }

    /*
    *   updates the recyclerView
    */
    public void updateList(ArrayList<VideosResult> results){
        trailers= new ArrayList<>();
        trailers.addAll(results);
        notifyDataSetChanged();
    }

    class ThumbnailViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterView;
        Context mContext;

        public ThumbnailViewHolder(View itemView, Context context){
            super(itemView);

            posterView= itemView.findViewById(R.id.ivTrailer);
            mContext= context;
            itemView.setOnClickListener(this);
        }

        /*
        *   binds the trailer image to the recyclerview
        */
        void bind(String trailerKey){
            try {
                Picasso.with(mContext)
                        .load(String.format(YOUTUBE_THUMBNAIL, trailerKey))
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
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListTrailerClick(clickedPosition);
        }
    }
}
