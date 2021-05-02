package com.example.moviesapp.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesapp.Adapters.ReviewAdapter;
import com.example.moviesapp.Adapters.TrailerAdapter;
import com.example.moviesapp.Model.*;
import com.example.moviesapp.R;
import com.example.moviesapp.Utilities.*;
import com.example.moviesapp.database.AppRepository;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moviesapp.ui.MainActivity.isOnline;

public class DetailActivity extends AppCompatActivity {


    private static final String PASSED_MOVIE_KEY = "passed movie";

    private static String IMAGE_API_URL = "https://image.tmdb.org/t/p/original";

    Trailers[] mTrailers;
    Review[] mReview;

    AppRepository mAppRepo;

    @BindView(R.id.tv_title)
    public TextView titleView;

    @BindView(R.id.tv_rating)
    public TextView ratingView;

    @BindView(R.id.tv_desc)
    public TextView descView;

    @BindView(R.id.iv_poster)
    public ImageView posterView;

    @BindView(R.id.iv_header)
    public ImageView headerView;

    @BindView(R.id.rv_trailers)
    public RecyclerView mTrailerRecycleView;


    @BindView(R.id.add_to_fav_btn)
    public ImageButton mFavBtn;

    @BindView(R.id.btn_rev_expnd)
    public ImageButton mRevExpBtn;

    @BindView(R.id.btn_trailer_expnd)
    public ImageButton mTrailerExpBtn;

    @BindView(R.id.rv_review)
    public RecyclerView mReviewRecycleView;



    public int reviewStatus = -1;
    public int trailerStatus = -1;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);

        mAppRepo = new AppRepository(getApplication());



        Intent intent = getIntent();
        final Movies movies = (Movies) intent.getSerializableExtra(PASSED_MOVIE_KEY);
        String title = movies.getTitle();
        String poster = movies.getPoster();
        String rating = movies.getRating();
        String desc = movies.getOverview();
        String movieID = movies.getMovieID();
        String header = movies.getHeader();

        ButterKnife.bind(this);


        Picasso.get().load(IMAGE_API_URL+poster).error(R.drawable.image_not_found)
                .into(posterView);
        Picasso.get().load(IMAGE_API_URL+header).error(R.drawable.image_not_found)
                .into(headerView);

        titleView.setText(title);
        ratingView.setText(rating);
        descView.setText(desc);

        Log.d("Detail Activity", "onCreate: "+movies.isFav());

        if (movies.isFav()){
            mFavBtn.setImageResource(R.drawable.ic_baseline_favorite_24_red);
        }


        if(isOnline()) {

            String trailerEndPoint = movieID + "/videos";
            loadTrailer(trailerEndPoint);
            if(MainActivity.query.equalsIgnoreCase("upcoming")){
                findViewById(R.id.tv_detail_label).setVisibility(View.GONE);
                findViewById(R.id.btn_rev_expnd).setVisibility(View.GONE);
            }
            else{
                String reviewEndPoint = movieID + "/reviews";
                loadReviews(reviewEndPoint);
            }

        }


        mRevExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewStatus==-1){
                    mReviewRecycleView.setVisibility(View.VISIBLE);
                    mRevExpBtn.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    reviewStatus =1;
                }
                else{
                    mReviewRecycleView.setVisibility(View.GONE);
                    mRevExpBtn.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    reviewStatus=-1;
                }}
        });

        mTrailerExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailerStatus==-1){
                    trailerStatus = 1;
                    mTrailerRecycleView.setVisibility(View.VISIBLE);
                    mTrailerExpBtn.setImageResource(R.drawable.ic_baseline_expand_less_24);

                }
                else{
                    trailerStatus = -1;
                    mTrailerRecycleView.setVisibility(View.GONE);
                    mTrailerExpBtn.setImageResource(R.drawable.ic_baseline_expand_more_24);

                }

            }
        });
        mFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movies.isFav()){
                    movies.setFav(false);
                    Snackbar snackbar = Snackbar.make(mFavBtn,"Removed from Favourites", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                    mAppRepo.delete(movies);
                    mFavBtn.setImageResource(R.drawable.ic_baseline_favorite_24);

                }
                else{
                    movies.setFav(true);
                    mAppRepo.insert(movies);
                    mFavBtn.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                    Snackbar snackbar = Snackbar.make(mFavBtn,"Added to Favourites", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();

                }
                Log.d("Detail Activity", "onClick: "+movies.isFav());
            }
        });


    }



    //Loads Review in the Background Threads
    private void loadReviews(String reviewEndPoint) {


       new ReviewAsync().execute(reviewEndPoint);


    }


    //Loads Trailer in the  Background Thread
    private void loadTrailer(String movieID) {


        new TrailerAsync().execute(movieID);

    }





    class ReviewAsync extends AsyncTask<String,Void,Review[]> {



        @Override
        protected Review[] doInBackground(String... strings) {
            String query = strings[0];
            URL url = NetworkUtil.buildUrl(query);

            String response;

            try {
                response = NetworkUtil.getHTTPSResponse(url);
                Log.i("Review", "run: "+response);
                mReview = JSONUtils.getJsonReview(response);

            } catch (IOException | JSONException e) {
                e.printStackTrace();

            }
            return mReview;
        }
        @Override
        protected void onPostExecute(Review[] reviews) {
            super.onPostExecute(reviews);
            updateReview();
        }

    }




    class TrailerAsync extends AsyncTask<String,Void,Trailers[]>{

        @Override
        protected void onPostExecute(Trailers[] trailers) {
            updateTrailers();
            super.onPostExecute(trailers);
        }
        @Override
        protected Trailers[] doInBackground(String... strings) {
            String query = strings[0];
            URL url = NetworkUtil.buildUrl(query);
            if (url==null){
                return null;
            }
            try {
                String response = NetworkUtil.getHTTPSResponse(url);

                //Log.i("ERROR", "doInBackground: "+response);
                mTrailers = JSONUtils.getJsonTrailers(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mTrailers;

        }

    }
    public void updateTrailers(){

        TrailerAdapter adapter = new TrailerAdapter(mTrailers);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mTrailerRecycleView.setAdapter(adapter);
        mTrailerRecycleView.setLayoutManager(layoutManager);
        mTrailerRecycleView.setHasFixedSize(true);

    }


    private void updateReview() {

        ReviewAdapter reviewAdapter = new ReviewAdapter(mReview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mReviewRecycleView.setAdapter(reviewAdapter);
        mReviewRecycleView.setLayoutManager(linearLayoutManager);
        mReviewRecycleView.setHasFixedSize(true);

    }

}