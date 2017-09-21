package com.pranjaldesai.popularmovies2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranjaldesai.popularmovies2.apiData.ReviewsResult;

import java.util.ArrayList;

public class ReviewsAdaptor extends RecyclerView.Adapter<ReviewsAdaptor.ReviewViewHolder> {
    private ArrayList<ReviewsResult> reviewsResults;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.review_grid_item;
        LayoutInflater inflater= LayoutInflater.from(context);

        View view= inflater.inflate(layoutID, parent, false);

        return new ReviewViewHolder(view, context);
    }

    /*
    *   binds the view and the exact author and message string
    */
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviewsResults.get(position).getAuthor(), reviewsResults.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsResults.size();
    }

    public ReviewsAdaptor(ArrayList<ReviewsResult>results){
        reviewsResults= results;
    }

    /*
    *   removes all the current data
    */
    public void removeData(){
        reviewsResults=null;
    }

    /*
    *   updates the recyclerView
    */
    public void updateList(ArrayList<ReviewsResult> results){
        reviewsResults= new ArrayList<>();
        reviewsResults.addAll(results);
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView authorTV, messageTV;
        Context mContext;

        public ReviewViewHolder(View itemView, Context context){
            super(itemView);

            authorTV= itemView.findViewById(R.id.review_author);
            messageTV= itemView.findViewById(R.id.review_message);
            mContext= context;
        }

        /*
        *   binds the author and message to the recyclerview
        */
        void bind(String author, String message){
            authorTV.setText(author);
            messageTV.setText(message);
        }

    }
}
