package com.pranjaldesai.popularmovies2.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Pranjal on 8/4/17.
 */

public class NetworkUtils {

    private final static String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";

    private final static String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie";

    private final static String PARAM_API = "api_key";
    private final static String VIDEO_PATH = "videos";
    private final static String REVIEW_PATH = "reviews";

    /**
     * Builds the URL used to query from the moviedb
     */
    public static URL buildUrl(String movieDBQuery, String apiKey) {

        Uri builtUri;

        if(movieDBQuery.equalsIgnoreCase("popular")){
            builtUri = Uri.parse(MOST_POPULAR_URL).buildUpon()
                    .appendQueryParameter(PARAM_API, apiKey)
                    .build();
        }else {
            builtUri = Uri.parse(TOP_RATED_URL).buildUpon()
                    .appendQueryParameter(PARAM_API, apiKey)
                    .build();
        }


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildVideoURL(String movieDBQuery, String apiKey){
        Uri builtUri;

        builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieDBQuery)
                .appendPath(VIDEO_PATH)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewURL(String movieDBQuery, String apiKey){
        Uri builtUri;

        builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieDBQuery)
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
