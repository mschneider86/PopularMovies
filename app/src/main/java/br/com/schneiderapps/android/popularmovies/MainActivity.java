package br.com.schneiderapps.android.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.network.AsyncTaskDelegate;
import br.com.schneiderapps.android.popularmovies.network.MovieAsyncTaskExecutor;
import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, AsyncTaskDelegate {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        //Poster width in px, same as the size informed in the query (w342), just a random value
        int posterWidth = (int) getResources().getDimension(R.dimen.movie_poster_width);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(posterWidth));

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mMovieList = new ArrayList<>();

        //If there's no savedInstance, calls the loadMoviesData method
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
        }else{
            //If there is a savedInstance, set the saved list into the adapter
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter.setMoviesData(mMovieList);
        }

    }

    private void loadMoviesData(String sortCriteria){

        //Check if the user has internet access
        if(NetworkUtils.isOnline(this)) {
            showMoviesDataView();

            //calls the async task executor to load the movies passing the criteria for the search
            new MovieAsyncTaskExecutor(this).execute(sortCriteria);

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

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("selectedMovie", selectedMovie);
        startActivity(intent);
    }

    @Override
    public void processStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void processResult(Object output) {

        mLoadingIndicator.setVisibility(View.GONE);

        if (output != null) {
            showMoviesDataView();

            mMovieList = (List<Movie>)output;

            mMovieAdapter.setMoviesData(mMovieList);
        } else {
            showErrorMessage(getResources().getString(R.string.error_message_default));
        }
    }

}
