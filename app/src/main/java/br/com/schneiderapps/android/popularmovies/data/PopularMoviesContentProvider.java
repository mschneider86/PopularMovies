package br.com.schneiderapps.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.schneiderapps.android.popularmovies.R;

import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.COLUMN_MOVIE_ID;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.CONTENT_URI;
import static br.com.schneiderapps.android.popularmovies.data.PopularMoviesContract.FavoriteMovies.TABLE_NAME;

public class PopularMoviesContentProvider extends ContentProvider {

    private PopularMoviesDbHelper mMoviesDbHelper;

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //directory
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES, MOVIES);

        //single item
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMoviesDbHelper = new PopularMoviesDbHelper(context);

        return true;
    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case MOVIES:
                long id  = db.insert(TABLE_NAME, null, values);
                if(id > 0){

                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException(getContext().getResources().getString(R.string.msg_error_insert_row) + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.msg_unknown_uri) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch(match){
            case MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.msg_unknown_uri) + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {

            case MOVIE_WITH_ID:

                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);

                String mSelection = COLUMN_MOVIE_ID + "=?";

                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(TABLE_NAME, mSelection, new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.msg_unknown_uri) + uri);
        }


        if (tasksDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
