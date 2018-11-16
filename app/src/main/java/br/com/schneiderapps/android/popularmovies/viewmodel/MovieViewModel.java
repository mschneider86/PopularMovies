package br.com.schneiderapps.android.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;
import br.com.schneiderapps.android.popularmovies.repositories.MovieRepository;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;

    private LiveData<List<MovieEntity>> mAllMovies;

    public MovieViewModel (Application application) {
        super(application);
        mRepository = new MovieRepository(application);

    }

    LiveData<List<MovieEntity>> getAllMovies(String sortCriteria) {
        return mRepository.getAllMovies(sortCriteria);
    }

    public LiveData<List<MovieEntity>> getFavoriteMovies(){
        return mRepository.getFavoriteMovies();
    }

    public void insertFavoriteMovie(MovieEntity movie) {
        mRepository.insertFavoriteMovie(movie);
    }

    public void deleteFavoriteMovie(MovieEntity movie){
        mRepository.deleteFavoriteMovie(movie);
    }


}