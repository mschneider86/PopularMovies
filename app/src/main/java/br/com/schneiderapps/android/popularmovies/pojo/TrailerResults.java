package br.com.schneiderapps.android.popularmovies.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResults {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<Trailer> listTrailers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getMovieTrailers() {
        return listTrailers;
    }

    public void setMovieTrailers(List<Trailer> movieTrailers) {
        this.listTrailers = movieTrailers;
    }
}
