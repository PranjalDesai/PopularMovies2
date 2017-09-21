package com.pranjaldesai.popularmovies2.apiData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pranjal on 8/11/17.
 */

public class MovieResult implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("vote_average")
    private double vote_average;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String release_date;

    private Reviews reviews;

    private Videos videos;

    private boolean shaded= false;

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }

    public double getVote_average(){
        return vote_average;
    }

    public String getPoster_path(){
        return poster_path;
    }

    public String getBackdrop_path(){
        return backdrop_path;
    }

    public boolean isAdult(){
        return adult;
    }

    public boolean isShaded(){
        return shaded;
    }

    public void setShaded(boolean shaded){
        this.shaded= shaded;
    }

    public MovieResult(){

    }

    public String getOverview(){
        return overview;
    }

    public String getRelease_date(){
        return release_date;
    }

    public void setReviewsResult(Reviews reviewsResult){
        this.reviews= reviewsResult;
    }

    public void setVideosResult(Videos videosResult){
        this.videos= videosResult;
    }

    public Reviews getReviews(){
        return reviews;
    }

    public Videos getVideos(){
        return videos;
    }

    public void setTitle(String title){
        this.title= title;
    }

    public void setId(int id){
        this.id= id;
    }

    public void setVote_average(double vote_average){
        this.vote_average= vote_average;
    }

    public void setPoster_path(String poster_path){
        this.poster_path= poster_path;
    }

    public void setBackdrop_path(String backdrop_path){
        this.backdrop_path= backdrop_path;
    }

    public void setAdult(boolean adult){
        this.adult= adult;
    }

    public void setOverview(String overview){
        this.overview= overview;
    }

    public void setRelease_date(String release_date){
        this.release_date= release_date;
    }
}
