package com.dk.devlighttest.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal + 1) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        int visibleThreshold = 5;
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            loading = true;
            onLoadMore(recyclerView);
        }
    }

    public abstract void onLoadMore(RecyclerView view);
}
