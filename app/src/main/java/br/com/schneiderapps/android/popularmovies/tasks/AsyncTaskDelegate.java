package br.com.schneiderapps.android.popularmovies.tasks;

public interface AsyncTaskDelegate<T> {

    void processStart();
    void processFinish(T object);
}
