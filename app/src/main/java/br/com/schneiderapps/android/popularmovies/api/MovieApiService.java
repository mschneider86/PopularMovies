package br.com.schneiderapps.android.popularmovies.api;

import android.arch.lifecycle.LiveData;

import java.util.List;


import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.pojo.MovieResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MovieResults> getPopularMovies();

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies();

    /*@GET("{sortCriteria}")
    Call<List<MovieEntity>> getMovies(@Path("sortCriteria") String sortCriteria);

    @GET
    Call<List<MovieEntity>> getMovies();*/
}
