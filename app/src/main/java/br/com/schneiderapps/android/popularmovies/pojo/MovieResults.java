package br.com.schneiderapps.android.popularmovies.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults {

    @SerializedName("results")
    @Expose
    private List<Movie> listMovies;

    public List<Movie> getListMovies() {
        return listMovies;
    }

    public void setListMovies(List<Movie> listMovies) {
        this.listMovies = listMovies;
    }
}
