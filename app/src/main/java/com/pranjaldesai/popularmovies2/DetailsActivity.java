package com.pranjaldesai.popularmovies2;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pranjaldesai.popularmovies2.apiData.MovieResult;
import com.pranjaldesai.popularmovies2.apiData.Reviews;
import com.pranjaldesai.popularmovies2.apiData.ReviewsResult;
import com.pranjaldesai.popularmovies2.apiData.Videos;
import com.pranjaldesai.popularmovies2.apiData.VideosResult;
import com.pranjaldesai.popularmovies2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_ADULT;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_BACKDROP;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_MOVIEID;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_OVERVIEW;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_POSTER;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_RELEASE;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_TITLE;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.COLUMN_VOTE;
import static com.pranjaldesai.popularmovies2.data.MovieContract.MovieDBEntry.CONTENT_URI;

public class DetailsActivity extends AppCompatActivity implements TrailerAdaptor.LinearItemClickListener{

    private ImageView bannerImageView, posterImageView;
    private static final String YOUTUBE_WEB= "http://www.youtube.com/watch?v=";
    private static final String YOUTUBE_MOBILE= "vnd.youtube:";
    private TextView userResultTV, releaseDateTV, plotTV, adultTV;
    private static final String mThemeName= "pref_theme_selection";
    private MovieResult mData;
    private final String bannerImage = "http://image.tmdb.org/t/p/original";
    private final String posterImage = "http://image.tmdb.org/t/p/w500/";
    private boolean mFab=true;
    private boolean firstCall=false, secondCall=false;
    private ProgressDialog progressDialog;
    private ArrayList<ReviewsResult> reviewsResults= new ArrayList<>();
    private ArrayList<VideosResult> videosResults= new ArrayList<>();
    private TrailerAdaptor trailerAdaptor;
    private ReviewsAdaptor reviewsAdaptor;
    private RecyclerView mRecyclerTrailerView, mRecyclerReviewsView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Sets the Theme
        final SharedPreferences mPreferences = getSharedPreferences(mThemeName, Context.MODE_PRIVATE);
        boolean currentThemeState= mPreferences.getBoolean(getString(R.string.themeSetting), false);
        if (currentThemeState) {
            setTheme(R.style.AppThemeDark);
        }else{
            setTheme(R.style.AppThemeLight);
        }

        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFab) {
                    fab.setImageDrawable(getDrawable(R.drawable.star_shaded));
                    if(mData!=null){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_TITLE, mData.getTitle());
                        contentValues.put(COLUMN_BACKDROP, mData.getBackdrop_path());
                        int adult;
                        if(mData.isAdult()){
                            adult=0;
                        }else{
                            adult=1;
                        }
                        contentValues.put(COLUMN_MOVIEID, mData.getId());
                        contentValues.put(COLUMN_OVERVIEW, mData.getOverview());
                        contentValues.put(COLUMN_POSTER, mData.getPoster_path());
                        contentValues.put(COLUMN_RELEASE, mData.getRelease_date());
                        contentValues.put(COLUMN_ADULT, adult);
                        contentValues.put(COLUMN_VOTE, mData.getVote_average());
                        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

                        if(uri != null) {
                            setSnackBarError(getString(R.string.insert));
                        }
                    }
                    mFab = false;
                }else{
                    fab.setImageDrawable(getDrawable(R.drawable.star_outline));
                    mFab = true;
                    if(mData!=null){
                        String stringId = Integer.toString(mData.getId());
                        Uri uri = CONTENT_URI;
                        uri = uri.buildUpon().appendPath(stringId).build();

                        int delete=getContentResolver().delete(uri, null, null);
                        if(delete!=0){
                            setSnackBarError(getString(R.string.delete));
                        }
                    }
                }
            }
        });
        setData();
    }

    /*
    *   initializes the views
    */
    private void init(){
        bannerImageView= (ImageView) findViewById(R.id.bannerImageView);
        posterImageView= (ImageView) findViewById(R.id.ivPoster);
        userResultTV= (TextView) findViewById(R.id.userRatingTV);
        releaseDateTV= (TextView) findViewById(R.id.releaseTV);
        plotTV= (TextView) findViewById(R.id.plot);
        adultTV= (TextView) findViewById(R.id.adultTV);
        progressDialog= new ProgressDialog(this);
    }

    /*
    *   grabs data object from the intent and then sets it to the views
    */
    private void setData(){
        Intent intent= getIntent();
        if(intent.hasExtra(getString(R.string.detailIntent))
                 && intent.getSerializableExtra(getString(R.string.detailIntent))!=null){
            mData= (MovieResult) intent.getSerializableExtra(getString(R.string.detailIntent));
            getSupportActionBar().setTitle(mData.getTitle());
            userResultTV.setText(Double.toString(mData.getVote_average()));
            releaseDateTV.setText(mData.getRelease_date());
            plotTV.setText(mData.getOverview());
            progressDialog.setTitle(getString(R.string.app_name));
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            if(mData.isAdult()){
                adultTV.setText(getString(R.string.yes));
            }else{
                adultTV.setText(getString(R.string.no));
            }
            if(mData.isShaded()){
                fab.setImageDrawable(getDrawable(R.drawable.star_shaded));
                mFab = false;
            }else{
                fab.setImageDrawable(getDrawable(R.drawable.star_outline));
                mFab = true;
            }
            makeTrailerReviewCall(0);
            makeTrailerReviewCall(1);
        }


        if(mData!=null) {
            try {
                Picasso.with(this)
                        .load(bannerImage +mData.getBackdrop_path())
                        .placeholder(R.drawable.ic_menu_slideshow)
                        .into(bannerImageView);

                Picasso.with(this)
                        .load(posterImage +mData.getPoster_path())
                        .placeholder(R.drawable.ic_menu_slideshow)
                        .into(posterImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void makeTrailerReviewCall(int apiType){
        URL searchURL;
        if(apiType==1){
            searchURL = NetworkUtils.buildVideoURL(Integer.toString(mData.getId()), getString(R.string.moviedb_api_key));
        }else{
            searchURL = NetworkUtils.buildReviewURL(Integer.toString(mData.getId()), getString(R.string.moviedb_api_key));
        }
        GetAPIData getAPIData= new GetAPIData();
        getAPIData.execute(searchURL);
    }

    private void setSnackBarError(String error){
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make((CoordinatorLayout)findViewById(R.id.movie_details_layout),
                        error, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void setJSONResult(String result){
        Videos videos= new Videos();
        Reviews reviews= new Reviews();
        Gson gson= new Gson();
        if(result.contains(getString(R.string.review))){
            firstCall=true;
            reviews= gson.fromJson(result.substring(6,result.length()),Reviews.class);
        }else if(result.contains(getString(R.string.video))){
            secondCall=true;
            videos= gson.fromJson(result.substring(6,result.length()),Videos.class);
        }

        if(firstCall && secondCall){
            progressDialog.dismiss();
            mData.setReviewsResult(reviews);
            mData.setVideosResult(videos);

            if(mData.getVideos()!=null) {
                videosResults = mData.getVideos().getResult();
                mRecyclerTrailerView = (RecyclerView) findViewById(R.id.rv_trailer_view);
                LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
                mRecyclerTrailerView.setLayoutManager(layoutManager);
                mRecyclerTrailerView.setHasFixedSize(true);
                trailerAdaptor = new TrailerAdaptor(this, videosResults);
                mRecyclerTrailerView.setAdapter(trailerAdaptor);
            }if(mData.getReviews()!=null) {
                reviewsResults = mData.getReviews().getResult();
                if(reviewsResults!=null && !reviewsResults.isEmpty()) {
                    mRecyclerReviewsView = (RecyclerView) findViewById(R.id.rv_review_view);
                    LinearLayoutManager layoutReviewManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    mRecyclerReviewsView.setLayoutManager(layoutReviewManager);
                    mRecyclerReviewsView.setHasFixedSize(true);
                    reviewsAdaptor = new ReviewsAdaptor(reviewsResults);
                    mRecyclerReviewsView.setAdapter(reviewsAdaptor);
                }
            }
        }

    }

    @Override
    public void onListTrailerClick(int clickedItemIndex) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_MOBILE + videosResults.get(clickedItemIndex).getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_WEB + videosResults.get(clickedItemIndex).getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }


    /*
    *   Making the api call on a different thread and then saving the strings.
    */
    private class GetAPIData extends AsyncTask<URL,Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];

            if(searchUrl.toString().contains(getString(R.string.reviews))){
                firstCall=true;
            }else if(searchUrl.toString().contains(getString(R.string.video))){
                secondCall=true;
            }

            String results;
            try {
                results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                return results;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String movieResults){
            if(movieResults!=null && !movieResults.equals("")){
                if(firstCall && !secondCall){
                    setJSONResult(getString(R.string.review)+movieResults);
                }else {
                    setJSONResult(getString(R.string.video)+movieResults);
                }
            }else {
                setSnackBarError(getString(R.string.mess_up));
            }

        }

    }

}
