package br.com.schneiderapps.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.com.schneiderapps.android.popularmovies.BuildConfig;


public class NetworkUtils {

    private static final String THE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org";

    private static final String API_KEY_QUERY_PARAM = "api_key";

    //Insert your ApiKey on gradle.properties
    private static final String MOVIE_DB_API_KEY = BuildConfig.ApiKey;



    private static final String T_PATH = "t";
    private static final String P_PATH = "p";
    private static final String IMAGE_SIZE_PATH = "w342";
    public static final String POPULAR_MOVIES_PATH = "popular";
    public static final String TOP_RATED_MOVIES_PATH = "top_rated";

    public static URL buildUrl(String... apiParams) {
        Uri.Builder uriBuilder = Uri.parse(THE_MOVIE_DB_URL).buildUpon();


        for (String p: apiParams) {
            uriBuilder.appendPath(p);
        }

        Uri builtUri = uriBuilder.appendQueryParameter(API_KEY_QUERY_PARAM, MOVIE_DB_API_KEY).build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildImageUri(String moviePosterUrl) {

        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(T_PATH)
                .appendPath(P_PATH)
                .appendPath(IMAGE_SIZE_PATH)
                .appendEncodedPath(moviePosterUrl)
                .build();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
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

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
