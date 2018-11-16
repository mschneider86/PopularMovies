package br.com.schneiderapps.android.popularmovies.api;

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieWebService {
    @GET("/users/{user}")
    Call<MovieEntity> getUser(@Path("user") String userId);
}
