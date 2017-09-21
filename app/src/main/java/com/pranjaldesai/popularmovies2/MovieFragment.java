package com.pranjaldesai.popularmovies2;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pranjaldesai.popularmovies2.apiData.MovieResult;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements MovieAdaptor.GridItemClickListener {

    private ArrayList<MovieResult> popularMovie, topRatedMovie;
    private ArrayList<String> popularImageURLString, topRatedImageURLString,
            popularTitleString, popularID, topRatedID, favID,
            topRatedTitleString, favImageURLString,favTitleString;
    private final String beginImage= "http://image.tmdb.org/t/p/w500/";
    private static final String mThemeName= "pref_theme_selection";
    private String mData="pop";
    private MovieAdaptor movieAdaptor;
    private RecyclerView mRecylerView;
    private GridLayoutManager gridLayoutManager;
    private View mView;
    private static int scrollPosition;
    private Activity activity;
    private SharedPreferences mPreferences;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView popularTextView, topRatedTextView, favTextView, apiFailTextView;
    private ImageView checkPopImg, checkTopImg, checkFavImg;
    private Cursor mCursor;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_movie, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        buildImagesAndTitle();
        buildFavorites();
        init(mView);
        setShaded();
        return mView;
    }

    /*
    *   Builds Title and Images ArrayList
    */
    private void buildImagesAndTitle() {
        popularImageURLString = new ArrayList<>();
        topRatedImageURLString = new ArrayList<>();
        popularTitleString = new ArrayList<>();
        topRatedTitleString = new ArrayList<>();
        topRatedID = new ArrayList<>();
        popularID= new ArrayList<>();
        if (popularMovie != null && topRatedMovie != null){
            for (MovieResult movieResult : popularMovie) {
                popularImageURLString.add(beginImage + movieResult.getPoster_path());
                popularTitleString.add(movieResult.getTitle());
                popularID.add(Integer.toString(movieResult.getId()));
            }
            for (MovieResult movieResult : topRatedMovie) {
                topRatedImageURLString.add(beginImage + movieResult.getPoster_path());
                topRatedTitleString.add(movieResult.getTitle());
                topRatedID.add(Integer.toString(movieResult.getId()));
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        buildImagesAndTitle();
        buildFavorites();
        updateRecyclerView(mData);
        setShaded();

    }

    private void buildFavorites(){
        favImageURLString= new ArrayList<>();
        favTitleString= new ArrayList<>();
        favID= new ArrayList<>();
        mCursor = getActivity().getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                COLUMN_TITLE);
        try {
            int titleID = mCursor.getColumnIndex(COLUMN_TITLE);
            int posterID = mCursor.getColumnIndex(COLUMN_POSTER);
            int movieID = mCursor.getColumnIndex(COLUMN_MOVIEID);
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                favImageURLString.add(beginImage + mCursor.getString(posterID));
                favTitleString.add(mCursor.getString(titleID));
                favID.add(Integer.toString(mCursor.getInt(movieID)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    *   Initializes the views
    */
    private void init(View view){
        checkPopImg= view.findViewById(R.id.popimg);
        checkTopImg= view.findViewById(R.id.topimg);
        checkFavImg= view.findViewById(R.id.favimg);
        popularTextView= view.findViewById(R.id.popradio);
        topRatedTextView= view.findViewById(R.id.topradio);
        favTextView= view.findViewById(R.id.favradio);
        apiFailTextView= view.findViewById(R.id.apiFailTV);

        mRecylerView = view.findViewById(R.id.rv_movie_view);
        gridLayoutManager= new GridLayoutManager(getContext(),3);
        mRecylerView.setLayoutManager(gridLayoutManager);
        mRecylerView.setHasFixedSize(true);

        if(getActivity()!=null) {
            mPreferences = getActivity().getSharedPreferences(mThemeName, Context.MODE_PRIVATE);
            mData= mPreferences.getString(getString(R.string.sort), "");
        }

        if(mData.equalsIgnoreCase(getString(R.string.sort_pop))) {
            checkTopImg.setVisibility(View.GONE);
            checkFavImg.setVisibility(View.GONE);
            checkPopImg.setVisibility(View.VISIBLE);
            movieAdaptor = new MovieAdaptor(this, popularTitleString, popularImageURLString,popularID);
        }else if (mData.equalsIgnoreCase(getString(R.string.sort_top))){
            checkTopImg.setVisibility(View.VISIBLE);
            checkFavImg.setVisibility(View.GONE);
            checkPopImg.setVisibility(View.GONE);
            movieAdaptor = new MovieAdaptor(this, topRatedTitleString, topRatedImageURLString, topRatedID);
        }else if (mCursor!=null && mData.equalsIgnoreCase(getString(R.string.sort_fav))){
            checkFavImg.setVisibility(View.VISIBLE);
            checkPopImg.setVisibility(View.GONE);
            checkTopImg.setVisibility(View.GONE);
            movieAdaptor = new MovieAdaptor(this, favTitleString,favImageURLString,favID);
        }

        mRecylerView.setAdapter(movieAdaptor);

        if(popularImageURLString.isEmpty() && popularTitleString.isEmpty()
                && topRatedTitleString.isEmpty() && topRatedImageURLString.isEmpty()){
            apiFailTextView.setVisibility(View.VISIBLE);
            apiFailTextView.setText(getString(R.string.api_fail));
        } else if(favImageURLString.isEmpty() && favTitleString.isEmpty()){
            apiFailTextView.setVisibility(View.VISIBLE);
            apiFailTextView.setText(getString(R.string.database_fail));
        }
        else{
            apiFailTextView.setVisibility(View.GONE);
            apiFailTextView.setText(getString(R.string.api_fail));
        }

        View bottomSheet= view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        listener();

    }

    /*
    *   Listener to listen for button clicks for sort
    */
    private void listener(){
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        popularTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= mPreferences.edit();
                editor.putString(getString(R.string.sort),getString(R.string.sort_pop));
                editor.apply();
                mData=getString(R.string.sort_pop);
                checkTopImg.setVisibility(View.GONE);
                checkFavImg.setVisibility(View.GONE);
                checkPopImg.setVisibility(View.VISIBLE);
                updateRecyclerView(mData);
            }
        });
        topRatedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= mPreferences.edit();
                editor.putString(getString(R.string.sort),getString(R.string.sort_top));
                editor.apply();
                mData=getString(R.string.sort_top);
                checkTopImg.setVisibility(View.VISIBLE);
                checkPopImg.setVisibility(View.GONE);
                checkFavImg.setVisibility(View.GONE);
                updateRecyclerView(mData);
            }
        });
        favTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= mPreferences.edit();
                editor.putString(getString(R.string.sort),getString(R.string.sort_fav));
                editor.apply();
                mData=getString(R.string.sort_fav);
                checkFavImg.setVisibility(View.VISIBLE);
                checkPopImg.setVisibility(View.GONE);
                checkTopImg.setVisibility(View.GONE);
                buildFavorites();
                updateRecyclerView(mData);
                setShaded();
            }
        });
    }

    /*
    *   Updates the Recycler View during orientation change or sort change.
    */
    private void updateRecyclerView(String isPopular){
        if(isPopular.equalsIgnoreCase(getString(R.string.sort_pop))){
            movieAdaptor.removeData();
            apiFailTextView.setVisibility(View.GONE);
            apiFailTextView.setText(getString(R.string.api_fail));
            movieAdaptor.updateList(popularTitleString,popularImageURLString,popularID);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if(isPopular.equalsIgnoreCase(getString(R.string.sort_top))){
            movieAdaptor.removeData();
            apiFailTextView.setVisibility(View.GONE);
            apiFailTextView.setText(getString(R.string.api_fail));
            movieAdaptor.updateList(topRatedTitleString,topRatedImageURLString,popularID);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            if(mCursor==null || mCursor.equals("") || mCursor.getCount()==0){
                apiFailTextView.setVisibility(View.VISIBLE);
                apiFailTextView.setText(getString(R.string.database_fail));
            }else{
                apiFailTextView.setVisibility(View.GONE);
                apiFailTextView.setText(getString(R.string.api_fail));
            }
            movieAdaptor.removeData();
            movieAdaptor.updateList(favTitleString,favImageURLString,favID);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(getString(R.string.scroll),gridLayoutManager.findFirstVisibleItemPosition());
        outState.putString(getString(R.string.data), mData);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            scrollPosition = savedInstanceState.getInt(getString(R.string.scroll));
            mData = savedInstanceState.getString(getString(R.string.data));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_movies_fragment);
            activity = getActivity();
        }
        buildFavorites();
        setShaded();
        updateRecyclerView(mData);
        mRecylerView.scrollToPosition(scrollPosition);

    }

    /*
    *   Sets the arguments from MainActivity
    */
    public void setArguments(ArrayList<MovieResult> movieResult, ArrayList<MovieResult> movieResult2){
        this.popularMovie = movieResult;
        this.topRatedMovie = movieResult2;
    }

    /*
    *   OnItemClick pass the object to new activity
    */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent= new Intent(getActivity(), DetailsActivity.class);
        if(mData.equalsIgnoreCase(getString(R.string.sort_pop))) {
            intent.putExtra(getString(R.string.detailIntent),popularMovie.get(clickedItemIndex));
            startActivity(intent);
        }else if (mData.equalsIgnoreCase(getString(R.string.sort_top))){
            intent.putExtra(getString(R.string.detailIntent),topRatedMovie.get(clickedItemIndex));
            startActivity(intent);
        }else{
            buildDatabaseResult();
        }
    }

    private void buildDatabaseResult(){
        String id=movieAdaptor.getTag();
        Uri uri = CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        Cursor cursor= getActivity().getContentResolver().query(uri,
                null,
                null,
                null,
                COLUMN_TITLE);

        if(cursor!=null){
            int titleID= mCursor.getColumnIndex(COLUMN_TITLE);
            int posterID= mCursor.getColumnIndex(COLUMN_POSTER);
            int ID= mCursor.getColumnIndex(COLUMN_MOVIEID);
            int adult= mCursor.getColumnIndex(COLUMN_ADULT);
            int backdrop= mCursor.getColumnIndex(COLUMN_BACKDROP);
            int overviewID= mCursor.getColumnIndex(COLUMN_OVERVIEW);
            int releaseID= mCursor.getColumnIndex(COLUMN_RELEASE);
            int voteID= mCursor.getColumnIndex(COLUMN_VOTE);
            MovieResult movieResult= new MovieResult();
            cursor.moveToFirst();
            if(cursor.getInt(adult)==0){
                movieResult.setAdult(true);
            }else{
                movieResult.setAdult(false);
            }
            movieResult.setTitle(cursor.getString(titleID));
            movieResult.setPoster_path(cursor.getString(posterID));
            movieResult.setId(cursor.getInt(ID));
            movieResult.setBackdrop_path(cursor.getString(backdrop));
            movieResult.setOverview(cursor.getString(overviewID));
            movieResult.setRelease_date(cursor.getString(releaseID));
            movieResult.setVote_average(Double.parseDouble(cursor.getString(voteID)));
            movieResult.setShaded(true);

            Intent intent= new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(getString(R.string.detailIntent),movieResult);
            startActivity(intent);
        }
    }

    private void setShaded(){
        if(mCursor!=null || !mCursor.equals("") || mCursor.getCount()>0){
            ArrayList<Integer> shadedID= new ArrayList<>();
            int i=0;
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                shadedID.add(mCursor.getInt(mCursor.getColumnIndex(COLUMN_MOVIEID)));
            }
            if(popularMovie!=null && topRatedMovie!=null){
                for(i=0; i<popularMovie.size(); i++){
                    popularMovie.get(i).setShaded(false);
                    for(Integer id:shadedID) {
                        if (popularMovie.get(i).getId()==id){
                            popularMovie.get(i).setShaded(true);
                        }
                    }
                }
                for(i=0; i<topRatedMovie.size(); i++){
                    topRatedMovie.get(i).setShaded(false);
                    for(Integer id:shadedID) {
                        if (topRatedMovie.get(i).getId()==id){
                            topRatedMovie.get(i).setShaded(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(activity!=null) {
            activity.getMenuInflater().inflate(R.menu.main, menu);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    /*
    *   onClick of the settings icon bottom sheet appear or it collapse
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(mBottomSheetBehavior.getPeekHeight()==0){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mBottomSheetBehavior.setPeekHeight(2);
            }else{
                mBottomSheetBehavior.setPeekHeight(0);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
