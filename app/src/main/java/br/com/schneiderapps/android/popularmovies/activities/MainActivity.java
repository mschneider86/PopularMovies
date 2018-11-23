package br.com.schneiderapps.android.popularmovies.activities;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.adapters.MovieAdapter;
import br.com.schneiderapps.android.popularmovies.R;
import br.com.schneiderapps.android.popularmovies.api.ApiClient;
import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.pojo.MovieResults;
import br.com.schneiderapps.android.popularmovies.tasks.AsyncTaskDelegate;
import br.com.schneiderapps.android.popularmovies.tasks.GetFavoritesListAsyncTaskExecutor;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, AsyncTaskDelegate {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private TextView mMoviesTryAgain;
    private LinearLayout mLinearError;

    private ProgressBar mLoadingIndicator;

    private List<Movie> mMovieList;

    private int menuItemIdSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mMoviesTryAgain = findViewById(R.id.tv_movies_try_again);
        mMoviesTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
            }
        });
        mLinearError = findViewById(R.id.linearError);

        //Poster width in px, same as the size informed in the query (w342), just a random value
        int posterWidth = (int) getResources().getDimension(R.dimen.movie_poster_width);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(posterWidth));

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        if(savedInstanceState != null){
            menuItemIdSelected = savedInstanceState.getInt(getResources().getString(R.string.saved_state_filter_key));
            invalidateOptionsMenu();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void loadMoviesData(String sortCriteria){

        //Check if the user has internet access
        if(NetworkUtils.isOnline(this)) {
            showMoviesDataView();

            mLoadingIndicator.setVisibility(View.VISIBLE);

            Call<MovieResults> call;

            switch(sortCriteria){

                case NetworkUtils.TOP_RATED__MOVIES_PATH:
                    call = ApiClient.getInstance().getTopRatedMovies();
                    break;

                case NetworkUtils.POPULAR_MOVIES_PATH:
                default:
                    call = ApiClient.getInstance().getPopularMovies();
            }

            call.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if(!response.isSuccessful()){
                        showErrorMessage(getResources().getString(R.string.msg_default_error));
                        return;
                    }

                    showMoviesDataView();

                    mMovieList = response.body().getListMovies();
                    mMovieAdapter.setMoviesData(mMovieList);

                    mLoadingIndicator.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    showErrorMessage(getResources().getString(R.string.msg_default_error));
                    mLoadingIndicator.setVisibility(View.GONE);
                }
            });

        }else
            showErrorMessage(getResources().getString(R.string.msg_no_network));
    }

    private void showMoviesDataView() {

        mLinearError.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesTryAgain.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message) {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mLinearError.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setText(message);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mMoviesTryAgain.setVisibility(View.VISIBLE);
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    private void loadFavoriteMovies(){

        new GetFavoritesListAsyncTaskExecutor(this).execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getResources().getString(R.string.saved_state_filter_key), menuItemIdSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movies, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if(menuItemIdSelected == R.id.action_sort_by_popular_movies){

            menuItem = menu.findItem(R.id.action_sort_by_popular_movies);

            if (!menuItem.isChecked()){
                menuItem.setChecked(true);
            }

            loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
            return true;

        }else if(menuItemIdSelected == R.id.action_sort_by_top_rated){
            menuItem = menu.findItem(R.id.action_sort_by_top_rated);

            if (!menuItem.isChecked()) {
                menuItem.setChecked(true);
            }

            loadMoviesData(NetworkUtils.TOP_RATED__MOVIES_PATH);
            return true;

        }else if(menuItemIdSelected == R.id.action_sort_by_favorite){
            menuItem = menu.findItem(R.id.action_sort_by_favorite);

            if (!menuItem.isChecked()) {
                menuItem.setChecked(true);
            }

            loadFavoriteMovies();
            return true;

        }else{
            loadMoviesData(NetworkUtils.POPULAR_MOVIES_PATH);
            return super.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        menuItemIdSelected = id;

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

        } else if(id == R.id.action_sort_by_favorite) {
            if (!item.isChecked())
                item.setChecked(true);

            loadFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie selectedMovie) {

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.selected_movie_extra_key), selectedMovie);
        startActivity(intent);
    }

    @Override
    public void processStart() {
        showMoviesDataView();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(Object object) {

        mMovieAdapter.setMoviesData((List<Movie>)object);
        mLoadingIndicator.setVisibility(View.GONE);
    }
}
