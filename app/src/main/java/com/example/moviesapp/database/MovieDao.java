package com.example.moviesapp.database;

import com.example.moviesapp.Model.Movies;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movies movie);


    @Delete
    void removeFav(Movies movie);


    @Query("SELECT * from fav_movies ORDER BY movieID ASC")
    LiveData<List<Movies>> getFavMovies();

    @Query("SELECT * FROM fav_movies " +
            "WHERE movieID = :ID")
    List<Movies> searchMovie(String ID);

    @Update(entity = Movies.class)
    void updateMovie(Movies movies);
}
