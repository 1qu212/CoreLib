package cn.xydzjnq.libcore.util.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class OnUpOrDownRefreshListener extends RecyclerView.OnScrollListener {
    private boolean isLoading;

    public OnUpOrDownRefreshListener(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.canScrollVertically(1)
                && !isLoading) {
            isLoading = true;
            //上拉加载更多操作
            //...
            onUpLoading(recyclerView, newState);
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.canScrollVertically(-1)
                && !isLoading) {
            isLoading = true;
            //下拉刷新操作，也可以使用SwipeRefreshLayout提供的下拉刷新
            //...
            onDownRefresh(recyclerView, newState);
        }
    }

    public abstract void onUpLoading(@NonNull RecyclerView recyclerView, int newState);

    public abstract void onDownRefresh(@NonNull RecyclerView recyclerView, int newState);
}
