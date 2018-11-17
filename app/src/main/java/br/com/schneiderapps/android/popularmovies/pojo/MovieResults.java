package br.com.schneiderapps.android.popularmovies.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults {

    @SerializedName("results")
    @Expose
    private List<Movie> resultMovies;

    public List<Movie> getResultMovies() {
        return resultMovies;
    }

    public void setResultMovies(List<Movie> resultMovies) {
        this.resultMovies = resultMovies;
    }
}
