package br.com.schneiderapps.android.popularmovies.tasks;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract;
import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.utilities.MovieUtils;

public class GetFavoritesListAsyncTaskExecutor extends AsyncTask<Void, Void, List<Movie>> {

    private AsyncTaskDelegate delegate;
    private Context context;

    public GetFavoritesListAsyncTaskExecutor(AsyncTaskDelegate delegate){
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
    protected List<Movie> doInBackground(Void... voids) {

        Cursor retCursor;
        try{

            retCursor = context.getContentResolver().query(PopularMoviesContract.FavoriteMovies.CONTENT_URI,
                    null,
            null,
                    null,
                    PopularMoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE);

            return MovieUtils.populateMovieList(retCursor);

        }catch(Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);

        if(delegate != null){
            delegate.processFinish(movies);
        }
    }
}
