package com.example.moviesapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesapp.Model.Movies;
import com.example.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieVH> {
    ArrayList<Movies> MovieList;
    onMovieClickListener MovieClickListener;

    private static final String IMAGE_API_URL = "https://image.tmdb.org/t/p/w500";
    public MoviesAdapter(ArrayList<Movies> data,onMovieClickListener movieClickListener){
        MovieList = data;
        MovieClickListener = movieClickListener;
    }


    @NonNull
    @Override
    public MoviesAdapter.MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.movie_list,parent,false);

        return new MovieVH(view, MovieClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MovieVH holder, int position) {

        holder.movieTitle.setText(MovieList.get(position).getTitle());
        holder.movieReleaseDate.setText(MovieList.get(position).getReleaseDate());

        String posterPath = MovieList.get(position).getPoster();

        Picasso.get().load(IMAGE_API_URL+posterPath)
                .error(R.drawable.image_not_found)
                .into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
         return MovieList.size();
    }

    private void empty() {

    }

    public static class MovieVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView movieTitle;
        public TextView movieReleaseDate;
        public onMovieClickListener onMovieClickListener;
        public MovieVH(@NonNull View itemView,onMovieClickListener onMovieClickListener ) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_movie_poster);

            movieTitle = itemView.findViewById(R.id.tv_movie_title);
            movieReleaseDate = itemView.findViewById(R.id.tv_movie_release_date);

            Log.i("Adaptor", String.valueOf(null == onMovieClickListener));
            this.onMovieClickListener = onMovieClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onMovieClickListener.onMovieClick(getAdapterPosition());
        }

    }
    public interface onMovieClickListener{
        void onMovieClick(int position);
    }

}
