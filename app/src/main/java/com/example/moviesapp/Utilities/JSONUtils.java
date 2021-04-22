package com.example.moviesapp.Utilities;

import android.util.Log;

import com.example.moviesapp.Model.Movies;
import com.example.moviesapp.Model.Review;
import com.example.moviesapp.Model.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class JSONUtils {

    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";

    private static final String TRAILER_NAME_KEY = "name";
    private static final String TRAILER_TYPE_KEY = "type";


    private static String IMAGE_API_URL = "https://image.tmdb.org/t/p/w500";


    private static String TITLE_KEY = "title";
    private static String RATING_KEY = "vote_average";
    private static String RELEASE_DATE_KEY = "release_date";
    private static String POSTER_KEY = "poster_path";
    private static String OVERVIEW_KEY = "overview";
    private static String MOVIE_ID_KEY = "id";

    public static Movies[] getJSONDetails(String response) throws JSONException {


        JSONObject jsonObject = new JSONObject(response);
        JSONArray result = jsonObject.getJSONArray("results");

        Movies[] movieresult = new Movies[result.length()];
        for (int i=0;i<result.length();i++){
            JSONObject json = result.getJSONObject(i);

            Movies movies = new Movies();

            movies.setMovieID(json.optString(MOVIE_ID_KEY));
            movies.setOverview(json.optString(OVERVIEW_KEY));
            movies.setPoster(IMAGE_API_URL+json.optString(POSTER_KEY));
            movies.setTitle(json.optString(TITLE_KEY));
            movies.setRating(json.get(RATING_KEY).toString());

            Log.i("JSON UTILS", "getJSONDetails: "+json.get(RATING_KEY).toString());
            Log.i("JSON UTILS", "getJSONDetails: "+movies.getRating());
            movies.setReleaseDate(json.optString(RELEASE_DATE_KEY));

            movieresult[i]=movies;
        }

        return movieresult;
    }


    public static  Review[] getJsonReview(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        JSONArray result = jsonObject.getJSONArray("results");

        Review[] reviews = new Review[result.length()];


        for (int i = 0; i < result.length(); i++) {
            JSONObject json = result.getJSONObject(i);

            Review review = new Review();
            String writerName = json.optString(AUTHOR_KEY);
            review.setAuthor(writerName);
            String content = json.optString(CONTENT_KEY);
            review.setContent(content);
            Log.i(TAG, "getJsonReview: "+writerName+", ");

            reviews[i] = review;
        }


        /*for (int i = 0;i<reviews.length;i++){
            Log.i("hii", "updateReview: "+reviews[i].getAuthor());
        }*/
        return reviews;
    }


    public static Trailers[] getJsonTrailers(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        JSONArray results = jsonObject.getJSONArray("results");

        Trailers[] trailers = new Trailers[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject json = results.getJSONObject(i);

            Trailers trailer = new Trailers();
            trailer.setTitle(json.optString(TRAILER_NAME_KEY));
            trailer.setType(json.optString(TRAILER_TYPE_KEY));
            trailer.setUrlLink(json.optString("key"));

            trailers[i] = trailer;
        }
        return trailers;

    }

}
