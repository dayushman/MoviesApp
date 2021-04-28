package com.example.moviesapp.database;

import android.app.Application;
import android.util.Log;

import com.example.moviesapp.Model.Movies;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AppRepository {


    private MovieDao mMoviesDao;
    private MoviesDB mMoviesDB;
    public static List<Movies> searchResult = null;
    private LiveData<List<Movies>> mAllFavMovies;

    public AppRepository(Application application){

        mMoviesDB = MoviesDB.getDatabase(application);
        mMoviesDao = mMoviesDB.getDao();

        mAllFavMovies = mMoviesDao.getFavMovies();

    }

    public LiveData<List<Movies>> getAllFavMovies() {
        return mAllFavMovies;
    }

    public void insert(Movies movies){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMoviesDB.getDao().insert(movies);
                Log.d("Repository", "run: Inserted");
            }
        });
    }

    public void delete(Movies movies){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMoviesDB.getDao().removeFav(movies);
                Log.d("Repository", "run: Deleted");
            }
        });
    }


    public List<Movies> searchMovie(String movieID){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                searchResult = mMoviesDB.getDao().searchMovie(movieID);
            }
        });
        return searchResult;
    }

    /*public void update(Movies movies){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMoviesDB.getDao().updateMovie(movies);
            }
        });
    }*/
}
