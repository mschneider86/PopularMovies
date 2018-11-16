package br.com.schneiderapps.android.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.com.schneiderapps.android.popularmovies.db.converter.DateConverter;
import br.com.schneiderapps.android.popularmovies.db.dao.MovieDao;
import br.com.schneiderapps.android.popularmovies.db.entities.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class PopularMoviesRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile PopularMoviesRoomDatabase INSTANCE;

    public static PopularMoviesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PopularMoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PopularMoviesRoomDatabase.class, "popular_movies_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
