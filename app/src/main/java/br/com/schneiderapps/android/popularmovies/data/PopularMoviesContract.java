package br.com.schneiderapps.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PopularMoviesContract {

    public static final String AUTHORITY = "br.com.schneiderapps.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class FavoriteMovies implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_MOVIE_ID = "movieId";

        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_POSTER_PATH = "posterPath";

        public static final String COLUMN_RELEASE_DATE = "releaseDate";

        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

        public static Uri buildFavoriteMovieUriWithId(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(movieId))
                    .build();
        }

    }
}
