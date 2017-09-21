package com.pranjaldesai.popularmovies2.apiData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranjal on 9/18/17.
 */

public class Videos implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private ArrayList<VideosResult> videosResults = new ArrayList<>();

    public ArrayList getResult(){
        return videosResults;
    }

    public int getId(){
        return id;
    }

}
