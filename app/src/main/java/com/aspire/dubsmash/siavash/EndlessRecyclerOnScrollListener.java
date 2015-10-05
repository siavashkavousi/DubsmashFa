package com.aspire.dubsmash.siavash;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sia on 10/2/15.
 */
public abstract class EndlessRecyclerOnScrollListener<T> extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    // The total number of items in the data set after the last load
    private int mPreviousTotal = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean mLoading = true;
    // The minimum amount of items to have below current scroll position before loading more.
    private int mVisibleThreshold = 5, mCurrentPage = 1;
    private T layoutManager;

    public EndlessRecyclerOnScrollListener(T layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = 0;
        int firstVisibleItem = 0;
        if (layoutManager instanceof LinearLayoutManager){
            totalItemCount = ((LinearLayoutManager) layoutManager).getItemCount();
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager){
            totalItemCount = ((GridLayoutManager) layoutManager).getItemCount();
            firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            // End has been reached
            onLoadMore(++mCurrentPage);
            mLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}
