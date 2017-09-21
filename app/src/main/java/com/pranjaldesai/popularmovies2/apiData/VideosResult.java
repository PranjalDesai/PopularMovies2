package com.pranjaldesai.popularmovies2.apiData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pranjal on 9/18/17.
 */

public class VideosResult implements Serializable {

    @SerializedName("key")
    private String key;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId(){
        return id;
    }

    public String getKey(){
        return key;
    }

    public String getName(){
        return name;
    }
}

