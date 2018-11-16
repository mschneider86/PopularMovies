package br.com.schneiderapps.android.popularmovies.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.db.PopularMoviesRoomDatabase;
import br.com.schneiderapps.android.popularmovies.db.dao.MovieDao;
import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<MovieEntity>> mAllMovies;

    public MovieRepository(Application application) {
        PopularMoviesRoomDatabase db = PopularMoviesRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();

    }

    public LiveData<List<MovieEntity>> getAllMovies(String sortCriteria) {
        return mMovieDao.getAllMovies(sortCriteria);
    }

    public LiveData<List<MovieEntity>> getFavoriteMovies(){
        return mMovieDao.getFavoriteMovies();
    }

    public void insertFavoriteMovie(MovieEntity movie) {
        new insertMovieAsyncTask(mMovieDao).execute(movie);
    }

    public void deleteFavoriteMovie(MovieEntity movie){
        new deleteMovieAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertMovieAsyncTask extends AsyncTask<MovieEntity, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertMovieAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieEntity... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }

    private static class deleteMovieAsyncTask extends AsyncTask<MovieEntity, Void, Void> {
        private MovieDao mAsyncTaskDao;

        deleteMovieAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieEntity... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }
    }
}
