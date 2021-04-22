package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moviesapp.Model.Review;
import com.example.moviesapp.R;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReViewHolder> {

    Review[] mReviews;
    public ReviewAdapter(Review[] reviews){
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ReViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list,parent,false);
        return new ReViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReViewHolder holder, int position) {
        holder.mContentTextView.setText(mReviews[position].getContent());
        holder.mWriterTextView.setText(mReviews[position].getAuthor());

    }

    @Override
    public int getItemCount() {
        return mReviews.length;
    }

    public class ReViewHolder extends RecyclerView.ViewHolder {
        TextView mContentTextView,mWriterTextView;
        public ReViewHolder(@NonNull View itemView) {

            super(itemView);
            mContentTextView = itemView.findViewById(R.id.review_tv_content);
            mWriterTextView = itemView.findViewById(R.id.review_tv_writer);

        }
    }
}
