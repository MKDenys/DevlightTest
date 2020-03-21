package com.dk.devlighttest.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollEventListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager;
    private boolean endLessStatus = true;


    /** is number of items that we could have after our
     *  current scroll position before we start loading
     more items */
    private int visibleThreshold = 5;
    /** to keep track of the page that we would like to
     * retrieve from a server our database
     * */
    private int currentPage = 0;
    /** total number of items that we retrieve lastly*/
    private int previousTotalItemCount = 0;
    /** indicating whether we are loading new dataset or not*/
    private boolean loading = true;
    /** the initial index of the page that'll start from */
    private int startingPageIndex = 0;

    /******* variables we could get from linearLayoutManager *******/

    /** the total number of items that we currently have on our recyclerview and we
     * get it from linearLayoutManager */
    private int totalItemCount;

    /** the position of last visible item in our view currently
     * get it from linearLayoutManager */
    private int lastVisibleItemPosition;

    public EndlessScrollEventListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (endLessStatus) {
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

            if (totalItemCount < previousTotalItemCount) {
                currentPage = startingPageIndex;
                previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    loading = true;
                }
            }

            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, recyclerView);
                loading = true;
            }
        }
    }

    // should be called if we do filter(search) to our list
    public void reset(){
        currentPage = startingPageIndex;
        previousTotalItemCount = 0;
        loading = true;
        endLessStatus = true;
    }

    public void turnOffEndless(){
        endLessStatus = false;
    }

    public abstract void onLoadMore(int pageNum, RecyclerView recyclerView);

}