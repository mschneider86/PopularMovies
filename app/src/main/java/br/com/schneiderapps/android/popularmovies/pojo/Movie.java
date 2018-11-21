package br.com.schneiderapps.android.popularmovies.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    private int movieId;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;


    public Movie(int movieId, String originalTitle, String posterPath, String overview, double voteAverage, String releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.voteAverage);
    }

    private Movie(Parcel in){
        this.movieId = in.readInt();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
