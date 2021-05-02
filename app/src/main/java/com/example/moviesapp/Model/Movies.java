package com.example.moviesapp.Model;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_movies")
public class Movies implements Serializable {

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "movieID")
    @PrimaryKey
    @NonNull
    private String movieID;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "poster")
    private String poster;


    @ColumnInfo(name = "header")
    private String header;

    @ColumnInfo(name="releaseDate")
    private String releaseDate;

    @ColumnInfo(name = "rating")
    private String rating;

    @ColumnInfo(name = "isFav")
    private boolean isFav;

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isFav() {
        return isFav;
    }

    public String getHeader() { return header; }

    public void setHeader(String header) { this.header = header; }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }
}
