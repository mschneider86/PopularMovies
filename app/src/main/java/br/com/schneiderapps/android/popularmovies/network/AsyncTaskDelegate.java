package br.com.schneiderapps.android.popularmovies.network;

public interface AsyncTaskDelegate<T> {

    void processStart();
    void processResult(T output);
}
