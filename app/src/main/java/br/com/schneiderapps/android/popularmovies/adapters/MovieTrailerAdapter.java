package br.com.schneiderapps.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.R;
import br.com.schneiderapps.android.popularmovies.pojo.Trailer;
import br.com.schneiderapps.android.popularmovies.utilities.NetworkUtils;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailersAdapterViewHolder>{


    private List<Trailer> mTrailersList;
    private Context mContext;
    private MovieTrailersAdapterOnClickHandler mClickHandler;

    public interface MovieTrailersAdapterOnClickHandler{
        void onClick(Trailer selectedTrailer);
    }

    public MovieTrailerAdapter(Context context, MovieTrailersAdapterOnClickHandler clickHandler){
        this.mContext = context;
        this.mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public MovieTrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailer_item, viewGroup, false);

        return new MovieTrailersAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailersAdapterViewHolder viewHolder, int position) {
        String movieTrailerUri = NetworkUtils.buildTrailerImageUrl(mTrailersList.get(position).getKey());

        Picasso.with(mContext)
                .load(movieTrailerUri)
                .placeholder(R.drawable.progress_animation)
                .into(viewHolder.mImageViewThumbnail);

        viewHolder.mTrailerTitle.setText(mTrailersList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(null == mTrailersList) return 0;
        return mTrailersList.size();
    }

    public void setMovieTrailersData(List<Trailer> trailersList){
        mTrailersList = trailersList;
        notifyDataSetChanged();
    }

    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageViewThumbnail;
        TextView mTrailerTitle;

        public MovieTrailersAdapterViewHolder(View itemView){
            super(itemView);

            mImageViewThumbnail = itemView.findViewById(R.id.movie_video_thumbnail);
            mImageViewThumbnail.setOnClickListener(this);

            mTrailerTitle = itemView.findViewById(R.id.tv_trailer_title);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer mSelectedTrailer = mTrailersList.get(adapterPosition);
            mClickHandler.onClick(mSelectedTrailer);

        }
    }
}


