package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moviesapp.Adapters.MoviesAdapter;
import com.example.moviesapp.Model.Movies;
import com.example.moviesapp.Model.Review;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.moviesapp.MainActivity.calculateNoOfColumns;

public class FavActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener {


    RecyclerView mFavRecycler;
    private Movies[] mMovieData;
    private static final String PASSED_MOVIE_KEY = "passed movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        mFavRecycler = findViewById(R.id.rv_fav_movies);
        AppRepository repo = new AppRepository(getApplication());
        repo.getAllFavMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(List<Movies> movies) {
                loadFavMovies(movies);
            }
        });

    }

    private void loadFavMovies(List<Movies> movies) {

        mMovieData = movies.toArray(new Movies[0]);
        MoviesAdapter moviesAdapter = new MoviesAdapter(mMovieData,this);
        int colWidth = calculateNoOfColumns(FavActivity.this);
        GridLayoutManager layoutManager =new GridLayoutManager(FavActivity.this,colWidth);

        mFavRecycler.setAdapter(moviesAdapter);
        mFavRecycler.setHasFixedSize(true);
        mFavRecycler.setLayoutManager(layoutManager);



    }


    @Override
    public void onMovieClick(int position) {

        Intent intent = new Intent(this,DetailActivity.class);

        intent.putExtra(PASSED_MOVIE_KEY,mMovieData[position]);
        startActivity(intent);
    }
}