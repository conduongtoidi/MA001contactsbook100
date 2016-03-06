package com.whatsoft.contactbook.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public interface Callback {
        void onLoadMore();

        void onScroll(int visibleItemCount, int pastVisibleItem);
    }

    private RecyclerView.LayoutManager layoutManager;
    private int pastVisibleItems;
    private int visibleItemCount;
    private boolean shouldProcess = true;
    private int scrollPos;
    private Callback callback;
    private boolean isLoading = true;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager, Callback callback) {
        this.layoutManager = layoutManager;
        this.callback = callback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (shouldProcess) {
            visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof LinearLayoutManager) {
                pastVisibleItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else {
                pastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
            }
            scrollPos = pastVisibleItems;
            if (isLoading) {
                if (totalItemCount > scrollPos) {
                    isLoading = false;
                    scrollPos = totalItemCount;
                }
            }
            if (!isLoading && (visibleItemCount + pastVisibleItems + 5) >= totalItemCount) {
                callback.onLoadMore();
                isLoading = true;
            }
        }
        callback.onScroll(visibleItemCount, pastVisibleItems);
    }

    public void setShouldProcess(boolean shouldProcess) {
        this.shouldProcess = shouldProcess;
    }

    public int getScrollPos() {
        return scrollPos;
    }

    public void reset() {
        visibleItemCount = 0;
        pastVisibleItems = 0;
    }
}
