package com.pranjaldesai.popularmovies2.apiData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranjal on 8/11/17.
 */

public class Movies implements Serializable{

    @SerializedName("results")
    private ArrayList<MovieResult> movieResult = new ArrayList<>();

    public ArrayList getResult(){
        return movieResult;
    }

}
