package com.magestore.app.lib.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Mike on 2/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract  class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    boolean mblnLockLazyLoading = false;
    boolean mblnEnableLazyLoading = true;
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public synchronized void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mblnLockLazyLoading || !mblnEnableLazyLoading) return;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;
            lockLazyLoading(true);
            onLoadMore(current_page);
            loading = true;
        }
    }

    /**
     * Khóa trạng thái ngừng lazyloading
     * @param lock
     */
    public void lockLazyLoading(boolean lock) {
        mblnLockLazyLoading = lock;
    }

    /**
     * Bật tắt lazy loading
     */
    public void enableLazyLoading(boolean enable) {
        mblnEnableLazyLoading = enable;
    }

    /**
     * Đang lock lazy loading hay không
     * @return
     */
    public boolean isLockLazyLoading() {
        return mblnLockLazyLoading;
    }

    /**
     * Cho phép lazy loading hay không
     * @return
     */
    public boolean isEnableLazyLoading() {
        return mblnEnableLazyLoading;
    }

    /**
     * Đặt page hiện tại
     */
    public void resetCurrentPage() {
        current_page = 1;
        previousTotal = 0;
    }

    public abstract void onLoadMore(int current_page);
}
