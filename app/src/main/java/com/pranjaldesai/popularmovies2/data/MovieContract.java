package com.pranjaldesai.popularmovies2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Pranjal on 9/20/17.
 */

public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.pranjaldesai.popularmovies2";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "movies";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MovieDBEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        // Task table and column names
        public static final String TABLE_NAME = "movies";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIEID = "movieID";
        public static final String COLUMN_VOTE = "voteAverage";
        public static final String COLUMN_RELEASE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_POSTER = "poster";

    }
}
