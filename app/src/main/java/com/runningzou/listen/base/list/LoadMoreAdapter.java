package com.runningzou.listen.base.list;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by zouzhihao on 2017/10/13.
 */

public abstract class LoadMoreAdapter<T> extends BaseAdapter<T> {

    private static final int FOOT_VIEW_TYPE = 0x678;

    private LoadMoreListener mLoadMoreListener;
    private LoadMoreStateChangeListener mLoadMoreStateChangeListener;

    private int mTotalItemCount;
    private int mLastVisibleItemPosition;
    private static final int VISIBLE_THRESHOLD = 2;
    private ViewDataBinding mFooterBinding;
    private LoadMoreState mState;

    @Override

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        //当前仅支持 LinearLayoutManager
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == SCROLL_STATE_IDLE && mLoadMoreListener != null) {
                        mTotalItemCount = linearLayoutManager.getItemCount();
                        mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                        if (mTotalItemCount <= (mLastVisibleItemPosition + VISIBLE_THRESHOLD)) {
                            setState(LoadMoreState.LOADING);
                            mLoadMoreListener.loadMoreData();
                        }
                    }

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_VIEW_TYPE) {
            mFooterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), footerLayout(), parent, false);
            return new BaseViewHolder(mFooterBinding);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.size() != 0 && position == mItems.size()) {
            return FOOT_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mItems.size() == 0) {
            return 0;
        }
        return mItems.size() + 1;
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    public abstract int footerLayout();

    public interface LoadMoreListener {
        void loadMoreData();
    }

    public interface LoadMoreStateChangeListener {
        void onStateChange(LoadMoreState state, ViewDataBinding footerBinding);
    }


    public void setLoadMoreStateChangeListener(LoadMoreStateChangeListener loadMoreStateChangeListener) {
        mLoadMoreStateChangeListener = loadMoreStateChangeListener;
    }

    public void setState(LoadMoreState state) {
        mState = state;
        if (mLoadMoreStateChangeListener != null) {
            mLoadMoreStateChangeListener.onStateChange(state,mFooterBinding);
        }
    }
}
