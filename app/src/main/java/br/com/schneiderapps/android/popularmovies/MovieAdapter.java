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

import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private List<MovieEntity> mMovieList;
    private Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler{
        void onClick(MovieEntity selectedMovie);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {

        mContext = context;
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
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }

    public void setMoviesData(List<MovieEntity> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
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
            int adapterPosition = getAdapterPosition();
            MovieEntity mSelectedMovie= mMovieList.get(adapterPosition);
            mClickHandler.onClick(mSelectedMovie);
        }
    }
}
