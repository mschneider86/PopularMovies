package br.com.schneiderapps.android.popularmovies.utilities;

import android.net.Uri;


public class NetworkUtils {

    private static final String THE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org";

    //TODO remove api key before commit
    private static final String API_KEY_QUERY_PARAM = "api_key";

    private static final String T_PATH = "t";
    private static final String P_PATH = "p";
    private static final String IMAGE_SIZE_PATH = "w185";
    public static final String POPULAR_MOVIES_PATH = "popular";
    public static final String TOP_RATED__MOVIES_PATH = "top_rated";

    public static Uri buildImageUri(String moviePosterUrl) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(T_PATH)
                .appendPath(P_PATH)
                .appendPath(IMAGE_SIZE_PATH)
                .appendEncodedPath(moviePosterUrl)
                .build();

        return builtUri;
    }
}
