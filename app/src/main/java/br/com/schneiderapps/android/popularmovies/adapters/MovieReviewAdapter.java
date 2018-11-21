package br.com.schneiderapps.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.schneiderapps.android.popularmovies.R;
import br.com.schneiderapps.android.popularmovies.pojo.Review;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {

    private List<Review> mReviewsList;
    private Context mContext;

    public MovieReviewAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public MovieReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review_item, viewGroup, false);

        return new MovieReviewAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapterViewHolder viewHolder, int position) {

        viewHolder.tvAuthor.setText(mReviewsList.get(position).getAuthor());
        viewHolder.tvContent.setText(mReviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if(null == mReviewsList) return 0;
        return mReviewsList.size();
    }

    public void setMovieReviewsData(List<Review> reviewsList){
        mReviewsList = reviewsList;
        notifyDataSetChanged();
    }

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor;
        TextView tvContent;

        public MovieReviewAdapterViewHolder(View itemView){
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.text_movie_review_author);
            tvContent = itemView.findViewById(R.id.text_movie_review_content);
        }
    }
}


