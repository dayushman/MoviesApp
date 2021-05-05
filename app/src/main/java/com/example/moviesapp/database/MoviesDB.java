package com.example.moviesapp.database;

import android.content.Context;

import com.example.moviesapp.Model.Movies;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movies.class}, version = 3, exportSchema = false)
public abstract class MoviesDB extends RoomDatabase {
    private static MoviesDB instance;
    private static final String databaseName = "fav_movies_database";
    private static final Object lock = new Object();

    public static MoviesDB getDatabase(Context context){
        if(instance == null){
            synchronized (lock){
                instance = Room.databaseBuilder(context,MoviesDB.class,databaseName)
                        .fallbackToDestructiveMigration().build();
            }
        }
        return instance;
    }


    public abstract MovieDao getDao();
}
