package br.com.schneiderapps.android.popularmovies.tasks;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract;
import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.utilities.MovieUtils;

public class GetFavoriteAsyncTaskExecutor extends AsyncTask<Integer, Integer, Movie> {

    private AsyncTaskDelegate delegate;
    private Context context;

    public GetFavoriteAsyncTaskExecutor(AsyncTaskDelegate delegate){
        this.context = ((Activity)delegate).getApplicationContext();
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        if(delegate != null) {
            delegate.processStart();
        }
    }

    @Override
    protected Movie doInBackground(Integer... integers) {
        Cursor retCursor;

        String[] mSelectionArgs = new String[]{String.valueOf(integers[0])};

        try{

            retCursor = context.getContentResolver().query(PopularMoviesContract.FavoriteMovies.buildFavoriteMovieUriWithId(integers[0]),
                    null,
                    null,
                    mSelectionArgs,
                    PopularMoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE);

            return MovieUtils.populateMovie(retCursor);

        }catch(Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie movie) {
        if(delegate != null) {
            delegate.processFinish(movie);
        }
    }
}
