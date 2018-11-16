package br.com.schneiderapps.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView mMoviePoster;
    TextView mMovieTitle;
    TextView mReleaseDate;
    TextView mVoteAverage;
    TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mMoviePoster = (ImageView)findViewById(R.id.imv_movie_poster);
        mMovieTitle = (TextView)findViewById(R.id.tv_movie_title);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mVoteAverage = (TextView)findViewById(R.id.tv_vote_average);
        mOverview = (TextView)findViewById(R.id.tv_overview);

        Intent intent = getIntent();

        //Check if the desired extra exists
        if(intent.hasExtra("selectedMovie")){
            MovieEntity selectedMovie = getIntent().getExtras().getParcelable("selectedMovie");

            Uri posterUrl = NetworkUtils.buildImageUri(selectedMovie.getPosterPath());

            //Load poster into imageview
            Picasso.with(this).load(posterUrl).into(mMoviePoster);

            //Load movie details
            mMovieTitle.setText(selectedMovie.getOriginalTitle());
            mReleaseDate.setText(selectedMovie.getReleaseDate());
            mVoteAverage.setText(Double.toString(selectedMovie.getVoteAverage()));
            mOverview.setText(selectedMovie.getOverview());
        }
    }
}
