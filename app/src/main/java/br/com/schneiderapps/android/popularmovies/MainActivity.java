package br.com.schneiderapps.android.popularmovies;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.utilities.JsonUtils;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //Poster width in px, same as the size informed in the query (w342), just a random value
        int posterWidth = 342;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(posterWidth));

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mMovieList = new ArrayList<>();

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
        }else{
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter.setMoviesData(mMovieList);
        }

    }

    private void loadMoviesData(String sortCriteria){

        if(NetworkUtils.isOnline(this)) {
            showMoviesDataView();

            new FetchPopularMoviesTask().execute(sortCriteria);

        }else
            showErrorMessage(getResources().getString(R.string.error_message_network));
    }

    private void showMoviesDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message) {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setText(message);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popular_movies) {
            if (!item.isChecked())
                item.setChecked(true);

            loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
            return true;

        } else if (id == R.id.action_sort_by_top_rated) {
            if (!item.isChecked())
                item.setChecked(true);

            loadMoviesData(NetworkUtils.TOP_RATED__MOVIES_PATH);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie selectedMovie) {

    }

    private class FetchPopularMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortCriteria = params[0];
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
        protected void onPostExecute(List<Movie> movieList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieList != null) {
                showMoviesDataView();

                mMovieList = movieList;

                mMovieAdapter.setMoviesData(mMovieList);
            } else {
                showErrorMessage(getResources().getString(R.string.error_message_default));
            }

        }

    }
}
