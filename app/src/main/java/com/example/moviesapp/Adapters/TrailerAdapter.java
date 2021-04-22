package com.example.moviesapp.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moviesapp.Model.Trailers;
import com.example.moviesapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    Trailers[] mTrailerData;

    public TrailerAdapter(Trailers[] trailers){

        mTrailerData = trailers;
    }
    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list,parent,false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        holder.titleTextView.setText(mTrailerData[position].getTitle());
        holder.typeTextView.setText(mTrailerData[position].getType());
    }


    @Override
    public int getItemCount() {
        return mTrailerData.length;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleTextView;
        TextView typeTextView;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_video_title);
            typeTextView = itemView.findViewById(R.id.tv_video_type);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            String id = mTrailerData[position].getUrlLink();
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                v.getContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                v.getContext().startActivity(webIntent);
            }
        }
    }

}
