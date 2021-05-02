package com.example.moviesapp.Listeners;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    int visibleThreshold = 5;
    int currentPage = 1;
    int previousTotal = 0;
    int visibleItemCount, totalItemCount,firstVisibleItemCount;
    boolean loading = true;
    GridLayoutManager gridLayoutManager;
    public EndlessScrollListener(GridLayoutManager gridLayoutManager){
        this.gridLayoutManager = gridLayoutManager;
        visibleThreshold = gridLayoutManager.getSpanCount() * visibleThreshold;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = recyclerView.getAdapter().getItemCount();
        firstVisibleItemCount = gridLayoutManager.findFirstVisibleItemPosition();

        if (loading && totalItemCount > previousTotal){
            loading = false;
            previousTotal = totalItemCount;
        }

        if (!loading && gridLayoutManager.findLastVisibleItemPosition()==totalItemCount-1){
            currentPage++;
            loadMoreData(currentPage);
            loading = true;
        }

    }

    public abstract void loadMoreData(int currentPage);
}
