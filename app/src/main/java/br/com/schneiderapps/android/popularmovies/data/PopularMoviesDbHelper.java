package br.com.schneiderapps.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popular_movies.db";

    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
                PopularMoviesContract.FavoriteMovies.TABLE_NAME + " (" +
                PopularMoviesContract.FavoriteMovies.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.FavoriteMovies.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                PopularMoviesContract.FavoriteMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                PopularMoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PopularMoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE + " REAL NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.FavoriteMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
