package br.com.schneiderapps.android.popularmovies.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;

@Dao
public interface MovieDao {

   @Insert
    void insertMovie(MovieEntity movie);


    @Delete
    void deleteMovie(MovieEntity movie);

    @Query("SELECT * from tb_favorite_movies ORDER BY original_title ASC")
    LiveData<List<MovieEntity>> getFavoriteMovies();

    LiveData<List<MovieEntity>> getAllMovies(String sortCriteria);
}
