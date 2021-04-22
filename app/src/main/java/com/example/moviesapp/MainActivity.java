package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.moviesapp.Model.Movies;
import com.example.moviesapp.Utilities.JSONUtils;
import com.example.moviesapp.Utilities.NetworkUtil;
import com.example.moviesapp.Adapters.MoviesAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener {


    private static final String PASSED_MOVIE_KEY = "passed movie";
    Movies[] mMovieData = null;
    private static String TAG = "MainActivity.java";
    private static String query = "popular";

//    MoviesAdapter.onMovieClickListener mMovieCLickListener;

    @BindView(R.id.iv_error)
    ImageView error;

    @BindView(R.id.pb_progress)
    ProgressBar progressBar;

    @BindView(R.id.rv_movie_list)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if(isOnline())
        {
            unerror();
            loadData();
        }
        else{
            error();

        }



    }
    //ERROR page on Visible on the screen
    public final void error() {
        error.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void unerror(){
        error.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_highest){
            query = "top_rated";
            loadData();
            return true;
        }
        if (item.getItemId()==R.id.menu_upcoming){
            query = "upcoming";
            loadData();
            return true;
        }
        if (item.getItemId()==R.id.menu_popular){
            query = "popular";
            loadData();
            return true;
        }
        if (item.getItemId() == R.id.fav_menu_btn){
            Intent intent = new Intent(MainActivity.this,FavActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void loadData() {
        new AsyncCustom().execute(query);
    }

    @Override
    public void onMovieClick(int position) {

       Intent intent = new Intent(this,DetailActivity.class);

        intent.putExtra(PASSED_MOVIE_KEY,mMovieData[position]);
        startActivity(intent);


    }
    class AsyncCustom extends AsyncTask<String,Void,Movies[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onPostExecute(Movies[] movies) {

            if (movies==null){
                error();
                return;
            }

            super.onPostExecute(movies);
            updateUI(movies);

        }


        @Override
        protected Movies[] doInBackground(String... strings) {
            String query = strings[0];
            URL url = NetworkUtil.buildUrl(query);
            if (url==null){
                return null;
            }
            try {
                String response = NetworkUtil.getHTTPSResponse(url);

                Log.i("ERROR", "doInBackground: "+query);
                mMovieData = JSONUtils.getJSONDetails(response);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mMovieData;
        }
    }


    private void updateUI(Movies[] movies) {

        MoviesAdapter moviesAdapter = new MoviesAdapter(movies,this);

        int colWidth = calculateNoOfColumns(MainActivity.this);
        GridLayoutManager layoutManager =new GridLayoutManager(MainActivity.this,colWidth);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(moviesAdapter);

        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    //Calculates the number of columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    //Checks the internet connection
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}