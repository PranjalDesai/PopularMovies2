package com.pranjaldesai.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pranjaldesai.popularmovies2.utilities.NetworkUtils;

import java.net.URL;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private final String popularMovie= "popular";
    private final String CUSTOMURL= "url";
    private final String topRatedMovie= "top_rated";
    private String popMovie, topMovie;
    private boolean firstCall, secondCall;
    private static final int POPULAR_LOADER = 22;
    private static final int TOP_LOADER = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeMovieURL(popularMovie);
        makeMovieURL(topRatedMovie);
    }

    /*
    *   Makes URL for topRated api or popular api
    */
    private void makeMovieURL(String urlString){
        URL searchURL= NetworkUtils.buildUrl(urlString,getString(R.string.moviedb_api_key));
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        if(urlString.equals(popularMovie)) {
            queryBundle.putString(CUSTOMURL, searchURL.toString());
            Loader<String> loader= loaderManager.getLoader(POPULAR_LOADER);
            if(loader== null){
                loaderManager.initLoader(POPULAR_LOADER, queryBundle,this);
            }else {
                loaderManager.restartLoader(POPULAR_LOADER,queryBundle,this);
            }
        }else {
            queryBundle.putString(CUSTOMURL , searchURL.toString());
            Loader<String> loader= loaderManager.getLoader(TOP_LOADER);
            if(loader== null){
                loaderManager.initLoader(TOP_LOADER, queryBundle,this);
            }else {
                loaderManager.restartLoader(TOP_LOADER,queryBundle,this);
            }
        }
    }

    /*
    *   Set JSON Strings and Starts intent for the MainActivity
    */
    private void setMovieJSONResult(String movieResult){

        if(movieResult.contains(getString(R.string.popular_prefix))){
            popMovie= movieResult.substring(7,movieResult.length());
            firstCall=true;
        }else if(movieResult.contains(getString(R.string.top_prefix))){
            secondCall=true;
            topMovie= movieResult.substring(7,movieResult.length());
        }

        if(firstCall && secondCall){
            Intent intent= new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(getString(R.string.popularMovieIntent), popMovie);
            intent.putExtra(getString(R.string.topRatedIntent), topMovie);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mTopRatedMovies, mPopularMovies;
            boolean mTop, mPop;

            @Override
            protected void onStartLoading(){
                if(args==null){
                    return;
                }

                if(mTopRatedMovies != null){
                    mTop=true;
                    mPop=false;
                    deliverResult(mTopRatedMovies);
                }else if(mPopularMovies != null){
                    mTop=false;
                    mPop=true;
                    deliverResult(mPopularMovies);
                }else {
                    forceLoad();
                }

            }

            @Override
            public String loadInBackground() {
                String urlString = args.getString(CUSTOMURL);
                String movieResults;
                try {
                    URL currURL= new URL(urlString);
                    movieResults = NetworkUtils.getResponseFromHttpUrl(currURL);
                    return movieResults;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
            /*
             *   Is internet available.
            */
            public boolean isOnline() {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo != null && netInfo.isConnectedOrConnecting();
            }

            @Override
            public void deliverResult(String json) {
                if(mPop){
                    mPopularMovies=json;
                    super.deliverResult(mPopularMovies);
                }else{
                    mTopRatedMovies= json;
                    super.deliverResult(mTopRatedMovies);
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(loader.getId()==22){
            if(data!=null && !data.equals("")){
                setMovieJSONResult(getString(R.string.popular_prefix)+data);
            }else {
                setMovieJSONResult("");
            }
        }else if(loader.getId()==23){
            if(data!=null && !data.equals("")){
                setMovieJSONResult(getString(R.string.top_prefix)+data);
            }else {
                setMovieJSONResult("");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
