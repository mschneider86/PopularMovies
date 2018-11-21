package br.com.schneiderapps.android.popularmovies.api;

import br.com.schneiderapps.android.popularmovies.pojo.MovieResults;
import br.com.schneiderapps.android.popularmovies.pojo.ReviewResults;
import br.com.schneiderapps.android.popularmovies.pojo.TrailerResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MovieResults> getPopularMovies();

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies();

    @GET("movie/{id}/videos")
    Call<TrailerResults> getMovieVideos(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    Call<ReviewResults> getMovieReviews(@Path("id") int movieId);

}
