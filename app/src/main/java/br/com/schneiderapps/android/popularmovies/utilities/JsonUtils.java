package br.com.schneiderapps.android.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;

public final class JsonUtils {

    private static final String JSON_OBJECT_RESULTS = "results";

    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    public static List<MovieEntity> getMovieListFromJson(String jsonString) throws JSONException {
        List<MovieEntity> movieList = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(jsonString);
        JSONArray moviesArray = moviesJson.getJSONArray(JSON_OBJECT_RESULTS);

        for (int i = 0; i < moviesArray.length(); i++) {
            int id;
            String originalTitle;
            String moviePoster;
            String overview;
            int voteAverage;
            String releaseDate;


            JSONObject movieJsonObject = moviesArray.getJSONObject(i);
            id = movieJsonObject.optInt(ID);
            originalTitle = movieJsonObject.optString(ORIGINAL_TITLE);
            moviePoster = movieJsonObject.optString(MOVIE_POSTER);
            overview = movieJsonObject.optString(OVERVIEW);
            voteAverage = movieJsonObject.optInt(VOTE_AVERAGE);
            releaseDate = movieJsonObject.optString(RELEASE_DATE);

            movieList.add(new MovieEntity(id, originalTitle, moviePoster, overview, voteAverage, releaseDate));
        }

        return movieList;
    }

}
