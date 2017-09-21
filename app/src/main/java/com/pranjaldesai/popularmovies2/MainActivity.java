package com.pranjaldesai.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.pranjaldesai.popularmovies2.apiData.MovieResult;
import com.pranjaldesai.popularmovies2.apiData.Movies;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private static final String mThemeName= "pref_theme_selection";
    private SwitchCompat mThemeSwitch;
    private Movies popularMovie, topRatedMovie;
    private ArrayList<MovieResult> popularMovieMovieResults, topRatedMovieMovieResults;

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

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //   Sort check
        SharedPreferences.Editor editor= mPreferences.edit();
        String sort= mPreferences.getString(getString(R.string.sort),"");
        if(sort.equalsIgnoreCase("") || sort.equalsIgnoreCase(getString(R.string.sort_pop))) {
            editor.putString(getString(R.string.sort), getString(R.string.sort_pop));
        }else{
            editor.putString(getString(R.string.sort), getString(R.string.sort_top));
        }
        editor.apply();

        //  Converts string to JSON and maps it to the Serializable Object
        Gson gson= new Gson();
        Intent intent= getIntent();
        if(intent.hasExtra(getString(R.string.popularMovieIntent))&& intent.hasExtra(getString(R.string.topRatedIntent))){
            String reciever= intent.getStringExtra(getString(R.string.popularMovieIntent));
            popularMovie= gson.fromJson(reciever,Movies.class);
            reciever= intent.getStringExtra(getString(R.string.topRatedIntent));
            topRatedMovie= gson.fromJson(reciever,Movies.class);
        }

        if (popularMovie != null && topRatedMovie != null) {
            popularMovieMovieResults = new ArrayList<>();
            topRatedMovieMovieResults = new ArrayList<>();
            popularMovieMovieResults =  popularMovie.getResult();
            topRatedMovieMovieResults = topRatedMovie.getResult();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MovieFragment movieFragment= new MovieFragment();
        if(popularMovieMovieResults !=null && topRatedMovieMovieResults !=null){
            movieFragment.setArguments(popularMovieMovieResults, topRatedMovieMovieResults);
        }


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                movieFragment).commit();

        //  Switch to toggle themes
        mThemeSwitch= (SwitchCompat) navigationView.getMenu().getItem(1).getActionView();
        mThemeSwitch.setChecked(currentThemeState);
        listner(mPreferences);
    }

    private void listner(final SharedPreferences mPreferences){
        mThemeSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor= mPreferences.edit();
                if(isChecked){
                    editor.putBoolean(getString(R.string.themeSetting),isChecked);
                    editor.apply();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else{
                    editor.putBoolean(getString(R.string.themeSetting),isChecked);
                    editor.apply();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movie) {
            MovieFragment movieFragment= new MovieFragment();
            if(popularMovieMovieResults !=null && topRatedMovieMovieResults !=null){
                movieFragment.setArguments(popularMovieMovieResults, topRatedMovieMovieResults);
            }
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    movieFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
