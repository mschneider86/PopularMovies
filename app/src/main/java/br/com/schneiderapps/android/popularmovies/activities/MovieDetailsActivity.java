package br.com.schneiderapps.android.popularmovies.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.adapters.MovieReviewAdapter;
import br.com.schneiderapps.android.popularmovies.adapters.MovieTrailerAdapter;
import br.com.schneiderapps.android.popularmovies.R;
import br.com.schneiderapps.android.popularmovies.api.ApiClient;
import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.pojo.ReviewResults;
import br.com.schneiderapps.android.popularmovies.pojo.Trailer;
import br.com.schneiderapps.android.popularmovies.pojo.TrailerResults;
import br.com.schneiderapps.android.popularmovies.tasks.AsyncTaskDelegate;
import br.com.schneiderapps.android.popularmovies.tasks.GetFavoriteAsyncTaskExecutor;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_MOVIE_ID;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_OVERVIEW;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_POSTER_PATH;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.CONTENT_URI;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.buildFavoriteMovieUriWithId;


public class MovieDetailsActivity extends AppCompatActivity implements MovieTrailerAdapter.MovieTrailersAdapterOnClickHandler, AsyncTaskDelegate{

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mReviewsTitle;
    private TextView mVideosTitle;

    private TextView mTrailersError;
    private TextView mTrailersTryAgain;
    private TextView mReviewsError;
    private TextView mReviewsTryAgain;

    private RecyclerView mRecyclerViewTrailers;
    private MovieTrailerAdapter mTrailersAdapter;

    private RecyclerView mRecyclerViewReviews;
    private MovieReviewAdapter mReviewsAdapter;

    private int movieId;
    private Context mContext;

    private List<Trailer> mTrailersList;
    private Movie selectedMovie;

    private boolean isMovieFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mContext = this;
        mMoviePoster = findViewById(R.id.imv_movie_poster);
        mMovieTitle = findViewById(R.id.tv_movie_title);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mVoteAverage = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);

        mReviewsTitle = findViewById(R.id.tv_reviews_title);
        mVideosTitle = findViewById(R.id.tv_trailers_title);

        mTrailersError = findViewById(R.id.tv_trailers_error);
        mTrailersTryAgain = findViewById(R.id.tv_trailers_try_again);
        mTrailersTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieTrailers();
            }
        });

        mReviewsError = findViewById(R.id.tv_reviews_error);
        mReviewsTryAgain = findViewById(R.id.tv_reviews_try_again);
        mReviewsTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieReviews();
            }
        });

        mRecyclerViewTrailers = findViewById(R.id.movie_videos);
        mRecyclerViewTrailers.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerTrailers = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTrailers.setLayoutManager(linearLayoutManagerTrailers);

        mTrailersAdapter = new MovieTrailerAdapter(mContext, this);
        mRecyclerViewTrailers.setAdapter(mTrailersAdapter);

        mRecyclerViewReviews = findViewById(R.id.movie_reviews);
        mRecyclerViewReviews.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerReviews = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReviews.setLayoutManager(linearLayoutManagerReviews);

        mReviewsAdapter = new MovieReviewAdapter(mContext);
        mRecyclerViewReviews.setAdapter(mReviewsAdapter);

        loadMovieDetails();

        checkIfMovieIsFavorite(selectedMovie.getMovieId());

        loadMovieTrailers();

        loadMovieReviews();
    }

    @Override
    public void onClick(Trailer selectedTrailer) {
        NetworkUtils.playTrailer(mContext, selectedTrailer.getKey());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movie_details, menu);

        //set the favorite icon
        if(isMovieFavorite){
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        }else{
            menu.getItem(0).setIcon(R.drawable.ic_favorite_border);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_favorite_movie){

            checkIfMovieIsFavorite(selectedMovie.getMovieId());
            if(isMovieFavorite){
                removeMovieFavorite();
                item.setIcon(R.drawable.ic_favorite_border);
            }else{
                addMovieFavorite();
                item.setIcon(R.drawable.ic_favorite);
            }

            return true;
        }else if (id == R.id.action_share_movie){
            //if the trailers list isn't empty, share the first one, otherwise, share the movie database page of the movie
            if(mTrailersList.size() > 0){
                NetworkUtils.shareMovieTrailer(mContext, selectedMovie.getOriginalTitle(), mTrailersList.get(0).getKey());
            }else{
                NetworkUtils.shareMovieLink(mContext, selectedMovie.getOriginalTitle(), String.valueOf(selectedMovie.getMovieId()));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieDetails(){
        Intent intent = getIntent();

        //Check if the desired extra exists
        if(intent.hasExtra(getResources().getString(R.string.selected_movie_extra_key))){
            selectedMovie = getIntent().getExtras().getParcelable(getResources().getString(R.string.selected_movie_extra_key));

            Uri posterUrl = NetworkUtils.buildImageUri(selectedMovie.getPosterPath());

            //get the selected movie id
            movieId = selectedMovie.getMovieId();

            //Load poster into imageview
            Picasso.with(this).load(posterUrl).into(mMoviePoster);

            //Load movie details
            mMovieTitle.setText(selectedMovie.getOriginalTitle());
            mReleaseDate.setText(selectedMovie.getReleaseDate());
            mVoteAverage.setText(Double.toString(selectedMovie.getVoteAverage()));
            mOverview.setText(selectedMovie.getOverview());
        }
    }

    private void loadMovieTrailers(){

        //Check if the user has internet access
        if(NetworkUtils.isOnline(mContext)) {
            showTrailersDataView();

            Call<TrailerResults> call;
            call = ApiClient.getInstance().getMovieVideos(movieId);


            call.enqueue(new Callback<TrailerResults>() {
                @Override
                public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                    if(!response.isSuccessful()){
                        showTrailersErrorMessage(getResources().getString(R.string.msg_default_error));
                        return;
                    }

                    showTrailersDataView();
                    mTrailersList = response.body().getMovieTrailers();


                    mTrailersAdapter.setMovieTrailersData(mTrailersList);

                    if(response.body().getMovieTrailers().size() > 0){
                        mVideosTitle.setVisibility(View.VISIBLE);
                    }else
                        mVideosTitle.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<TrailerResults> call, Throwable t) {
                    showTrailersErrorMessage(getResources().getString(R.string.msg_default_error));
                }
            });

        }else{
            showTrailersErrorMessage(getResources().getString(R.string.msg_no_network_short));
        }

    }

    private void loadMovieReviews(){
        //Check if the user has internet access
        if(NetworkUtils.isOnline(mContext)) {
            showReviewsDataView();

            Call<ReviewResults> call;

            call = ApiClient.getInstance().getMovieReviews(movieId);


            call.enqueue(new Callback<ReviewResults>() {
                @Override
                public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                    if(!response.isSuccessful()){
                        showReviewsErrorMessage(getResources().getString(R.string.msg_default_error));
                        return;
                    }

                    showReviewsDataView();

                    mReviewsAdapter.setMovieReviewsData(response.body().getListReviews());

                    if(response.body().getListReviews().size() > 0){
                        mReviewsTitle.setVisibility(View.VISIBLE);
                    }else
                        mReviewsTitle.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ReviewResults> call, Throwable t) {
                    showReviewsErrorMessage(getResources().getString(R.string.msg_default_error));
                }
            });

        }else {
            showReviewsErrorMessage(getResources().getString(R.string.msg_no_network_short));
        }
    }

    private void addMovieFavorite(){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MOVIE_ID, selectedMovie.getMovieId());
            contentValues.put(COLUMN_ORIGINAL_TITLE, selectedMovie.getOriginalTitle());
            contentValues.put(COLUMN_OVERVIEW, selectedMovie.getOverview());
            contentValues.put(COLUMN_POSTER_PATH, selectedMovie.getPosterPath());
            contentValues.put(COLUMN_RELEASE_DATE, selectedMovie.getReleaseDate());
            contentValues.put(COLUMN_VOTE_AVERAGE, selectedMovie.getVoteAverage());

            Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

            if (uri != null) {
                Toast.makeText(mContext, getResources().getString(R.string.msg_saved_favorite), Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(mContext, getResources().getString(R.string.msg_error_save_favorite), Toast.LENGTH_LONG).show();
        }
    }

    private void removeMovieFavorite(){

        try {
            int result = getContentResolver().delete(buildFavoriteMovieUriWithId(selectedMovie.getMovieId()), null, null);

            if (result > 0) {
                Toast.makeText(mContext, getResources().getString(R.string.msg_removed_favorite), Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(mContext, getResources().getString(R.string.msg_error_remove_favorite), Toast.LENGTH_LONG).show();
        }
    }

    private void checkIfMovieIsFavorite(int movieId){
        new GetFavoriteAsyncTaskExecutor(this).execute(movieId);
    }

    private void showTrailersDataView(){
        mRecyclerViewTrailers.setVisibility(View.VISIBLE);

        mTrailersError.setVisibility(View.GONE);
        mTrailersTryAgain.setVisibility(View.GONE);
    }

    private void showTrailersErrorMessage(String message){
        mTrailersError.setText(message);
        mTrailersError.setVisibility(View.VISIBLE);
        mTrailersTryAgain.setVisibility(View.VISIBLE);

        mRecyclerViewTrailers.setVisibility(View.GONE);
    }

    private void showReviewsDataView(){
        mRecyclerViewReviews.setVisibility(View.VISIBLE);

        mReviewsError.setVisibility(View.GONE);
        mReviewsTryAgain.setVisibility(View.GONE);
    }

    private void showReviewsErrorMessage(String message){
        mReviewsError.setText(message);
        mReviewsError.setVisibility(View.VISIBLE);
        mReviewsTryAgain.setVisibility(View.VISIBLE);

        mRecyclerViewReviews.setVisibility(View.GONE);
    }

    @Override
    public void processStart() {

    }

    @Override
    public void processFinish(Object object) {

        isMovieFavorite = object != null;
        invalidateOptionsMenu();

    }
}
