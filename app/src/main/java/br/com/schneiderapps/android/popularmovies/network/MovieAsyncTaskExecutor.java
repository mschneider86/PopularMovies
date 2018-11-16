package br.com.schneiderapps.android.popularmovies.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;
import br.com.schneiderapps.android.popularmovies.utilities.JsonUtils;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MovieAsyncTaskExecutor extends AsyncTask<String, Void, List<MovieEntity>> {

    private AsyncTaskDelegate delegate;
    private Context context;

    public MovieAsyncTaskExecutor(AsyncTaskDelegate taskDelegate){
        this.context = ((Activity)taskDelegate).getApplicationContext();
        this.delegate = taskDelegate;
    }

    @Override
    protected void onPreExecute() {
        if(null != delegate) {
            delegate.processStart();
        }
    }

    @Override
    protected List<MovieEntity> doInBackground(String... strings) {
        if (strings.length == 0) {
            return null;
        }

        String sortCriteria = strings[0];
        URL movieRequestUrl = NetworkUtils.buildUrl(sortCriteria);

        try {
            String jsonBuiltURLResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            return JsonUtils.getMovieListFromJson(jsonBuiltURLResponse);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieEntity> movies) {
        super.onPostExecute(movies);
        if(delegate != null)
            delegate.processResult(movies);
    }
}
