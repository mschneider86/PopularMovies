package br.com.schneiderapps.android.popularmovies.utilities;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.pojo.Movie;

import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_MOVIE_ID;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_OVERVIEW;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_POSTER_PATH;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE;

public class MovieUtils {

    public static List<Movie> populateMovieList(Cursor cursor) {

        List<Movie> movieList = new ArrayList<>();

        try {

            while (cursor.moveToNext()) {

                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID);
                int originalTitleIndex = cursor.getColumnIndexOrThrow(COLUMN_ORIGINAL_TITLE);
                int posterPathIndex = cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH);
                int overviewIndex = cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW);
                int voteAverageIndex = cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE);
                int releaseDateIndex = cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE);

                movieList.add(new Movie(
                        cursor.getInt(idIndex),
                        cursor.getString(originalTitleIndex),
                        cursor.getString(posterPathIndex),
                        cursor.getString(overviewIndex),
                        cursor.getDouble(voteAverageIndex),
                        cursor.getString(releaseDateIndex)
                ));
            }
        } finally {
            cursor.close();
        }

        return movieList;
    }

    public static Movie populateMovie(Cursor cursor) {

        try {

            if(cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID);
                int originalTitleIndex = cursor.getColumnIndexOrThrow(COLUMN_ORIGINAL_TITLE);
                int posterPathIndex = cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH);
                int overviewIndex = cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW);
                int voteAverageIndex = cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE);
                int releaseDateIndex = cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE);

                return new Movie(
                        cursor.getInt(idIndex),
                        cursor.getString(originalTitleIndex),
                        cursor.getString(posterPathIndex),
                        cursor.getString(overviewIndex),
                        cursor.getDouble(voteAverageIndex),
                        cursor.getString(releaseDateIndex)
                );
            }

            return null;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }

    }
}
