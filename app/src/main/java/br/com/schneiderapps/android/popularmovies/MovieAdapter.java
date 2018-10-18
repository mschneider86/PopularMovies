package br.com.schneiderapps.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

import br.com.schneiderapps.android.popularmovies.pojo.Movie;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private List<Movie> mMovieList;
    private Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(String movieClicked);
    }

    public MovieAdapter(Context context, List<Movie> movieList, MovieAdapterOnClickHandler clickHandler) {

        mContext = context;
        mMovieList = movieList;
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item_movie, viewGroup, false);

        return new MovieAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Uri moviePosterUri = NetworkUtils.buildImageUri(mMovieList.get(position).getPosterPath());
        Picasso.with(mContext).load(moviePosterUri).into(movieAdapterViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_movie_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
