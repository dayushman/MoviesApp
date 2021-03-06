package com.example.moviesapp.ui;

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

import com.example.moviesapp.Listeners.EndlessScrollListener;
import com.example.moviesapp.Model.Movies;
import com.example.moviesapp.R;
import com.example.moviesapp.Utilities.JSONUtils;
import com.example.moviesapp.Utilities.NetworkUtil;
import com.example.moviesapp.Adapters.MoviesAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener {


    private static final String PASSED_MOVIE_KEY = "passed movie";
    ArrayList<Movies> mMovieData = null;
    private static final String TAG = "MainActivity.java";
    public static String query = "popular";
    private static int page = 1;
    private static MoviesAdapter mMoviesAdapter;
    public static GridLayoutManager mGridLayoutManager;

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

        checkConnection();


    }

    private void checkConnection() {
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

        if (item.getItemId()==R.id.menu_highest && !query.equalsIgnoreCase("top_rated")){
            query = "top_rated";
            reset();
            loadData();
            return true;
        }
        if (item.getItemId()==R.id.menu_upcoming && !query.equalsIgnoreCase("upcoming")){
            query = "upcoming";
            reset();
            loadData();
            return true;
        }
        if (item.getItemId()==R.id.menu_popular && !query.equalsIgnoreCase("popular")){
            query = "popular";
            reset();
            loadData();
            return true;
        }
        if (item.getItemId() == R.id.fav_menu_btn){
            reset();
            Intent intent = new Intent(MainActivity.this, FavActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        page = 1;
        mMovieData = null;
        mGridLayoutManager = null;
        mMoviesAdapter = null;
    }


    private void loadData() {
        new AsyncCustom().execute(query);
    }

    @Override
    public void onMovieClick(int position) {

       Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra(PASSED_MOVIE_KEY, mMovieData.get(position));
        startActivity(intent);


    }
    class AsyncCustom extends AsyncTask<String,Void,ArrayList<Movies>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error.setVisibility(View.INVISIBLE);
            if (mMovieData == null){
                progressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {

            if (movies==null){
                error();
                return;
            }

            super.onPostExecute(movies);
            if (mGridLayoutManager == null)
                updateUI();
            else{
                mMoviesAdapter.setMovieList(mMovieData);
                mMoviesAdapter.notifyDataSetChanged();
            }


        }


        @Override
        protected ArrayList<Movies> doInBackground(String... strings) {
            String query = strings[0];
            URL url = NetworkUtil.buildUrl(query,page);
            if (url==null){
                return null;
            }
            try {
                String response = NetworkUtil.getHTTPSResponse(url);

                Movies[] data = JSONUtils.getJSONDetails(response);
                Log.i("ERROR", "doInBackground: "+query);
                if (mMovieData == null || Integer.parseInt(data[0].getMovieID()) == Integer.parseInt(mMovieData.get(0).getMovieID())){
                    mMovieData = new ArrayList<>(Arrays.asList(data));
                }
                Log.e(TAG, "doInBackground: "+Arrays.toString(data) );
                mMovieData.addAll(Arrays.asList(data));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mMovieData;
        }
    }


    private void updateUI() {

        mMoviesAdapter = new MoviesAdapter(mMovieData,this);


        int colWidth = calculateNoOfColumns(MainActivity.this);
        mGridLayoutManager =new GridLayoutManager(MainActivity.this,colWidth);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);
        EndlessScrollListener scrollListener = new EndlessScrollListener(mGridLayoutManager) {
            @Override
            public void loadMoreData(int currentPage) {
                page = currentPage;
                loadData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    //Calculates the number of columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
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