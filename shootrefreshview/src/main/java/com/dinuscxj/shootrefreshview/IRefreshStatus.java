package com.dinuscxj.shootrefreshview;

/**
 * copied from
 * https://github.com/dinuscxj/RecyclerRefreshLayout/blob/master/recyclerrefreshlayout/src/main/java/com/dinuscxj/refresh/IRefreshStatus.java
 */
public interface IRefreshStatus {
    /**
     * When the content view has reached to the start point and refresh has been completed, view will be reset.
     */
    void reset();

    /**
     * Refresh View is refreshing
     */
    void refreshing();

    /**
     * Refresh View is dropped down to the refresh point
     */
    void pullToRefresh();

    /**
     * Refresh View is released into the refresh point
     */
    void releaseToRefresh();

    /**
     * @param pullDistance The drop-down distance of the refresh View
     * @param pullProgress The drop-down progress of the refresh View and the pullProgress may be more than 1.0f
     *                     pullProgress = pullDistance / refreshTargetOffset
     */
    void pullProgress(float pullDistance, float pullProgress);

    /**
     * Refresh View refresh is completed
     */
    void refreshComplete();
}