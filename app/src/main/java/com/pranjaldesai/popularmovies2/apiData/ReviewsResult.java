package com.pranjaldesai.popularmovies2.apiData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pranjal on 9/18/17.
 */

public class ReviewsResult implements Serializable {

    @SerializedName("author")
    private String author;

    @SerializedName("id")
    private String id;

    @SerializedName("content")
    private String content;

    public String getAuthor(){
        return author;
    }

    public String getId(){
        return id;
    }

    public String getContent(){
        return content;
    }
}

