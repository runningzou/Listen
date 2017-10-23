package com.runningzou.listen.base.list;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.runningzou.listen.base.rxjava.RxCommand;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zouzhihao on 2017/10/13.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mItems = new ArrayList<>();
    protected List<T> mUpdateItems;

    private Boolean mRefresh;

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;

    private RxCommand<DiffUtil.DiffResult, Object> mDiffResultRxCommand;

    private OnItemBindListener mOnItemBindListener;

    public BaseAdapter() {

        mDiffResultRxCommand = RxCommand.create(new Function<Object, Observable<DiffUtil.DiffResult>>() {
            @Override
            public Observable<DiffUtil.DiffResult> apply(@NonNull Object o) throws Exception {
                return Observable.create(new ObservableOnSubscribe<DiffUtil.DiffResult>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<DiffUtil.DiffResult> e) throws Exception {
                        BaseCallBack callBack = new BaseCallBack(mItems, mUpdateItems);
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callBack, true);
                        e.onNext(diffResult);
                        e.onComplete();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        });


        mDiffResultRxCommand.switchToLatest()
                .subscribe(new Consumer<DiffUtil.DiffResult>() {
                    @Override
                    public void accept(DiffUtil.DiffResult result) throws Exception {
                        result.dispatchUpdatesTo(BaseAdapter.this);
                        mItems = mUpdateItems;
                        if (mRefresh) {
                            mLinearLayoutManager.scrollToPosition(0);
                        }
                    }
                });

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), itemLayout(), parent, false);
        return new BaseViewHolder(binding);
    }


    protected abstract int itemLayout();

    protected abstract void bindItem(ViewDataBinding binding, T item);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (position < mItems.size()) {
            bindItem(holder.binding, mItems.get(position));
            holder.binding.executePendingBindings();
        }

        if (mOnItemBindListener != null) {
            if (position < mItems.size()) {
                mOnItemBindListener.itemBindListener(holder.binding, mItems.get(position));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
        mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

    }

    //更新数据
    private void replace(final List<T> update, final boolean refresh) {

        mUpdateItems = update;
        mRefresh = refresh;

        if (mItems == null) {
            if (update == null) {
                return;
            }
            mItems = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = mItems.size();
            mItems = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {

            mDiffResultRxCommand.execute(null);

        }
    }

    public void loadMore(List<T> events) {

        int startPostion = mItems.size();

        mItems.addAll(events);

        notifyItemRangeInserted(startPostion, events.size());

    }

    public void loadMore(T t) {

        int pos = mItems.size();
        mItems.add(t);

        notifyItemInserted(pos);

    }

    public void update(int index) {
        notifyItemChanged(index);
    }

    public List<T> getItems() {
        return mItems;
    }

    public void refresh(List<T> events) {
        replace(events, true);
    }


    public void deleteItem(T t) {
        int index = mItems.indexOf(t);
        mItems.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    protected boolean areItemsTheSame(T oldItem, T newItem) {
        return oldItem == newItem;
    }

    protected boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }

    public class BaseCallBack extends DiffUtil.Callback {

        private List<T> mOldItems;
        private List<T> mUpdateItems;

        public BaseCallBack(List<T> oldItems, List<T> updateItems) {
            mOldItems = oldItems;
            mUpdateItems = updateItems;
        }

        @Override
        public int getOldListSize() {
            return mOldItems.size();
        }

        @Override
        public int getNewListSize() {
            return mUpdateItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return BaseAdapter.this.areItemsTheSame(mOldItems.get(oldItemPosition), mUpdateItems.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return BaseAdapter.this.areContentsTheSame(mOldItems.get(oldItemPosition), mUpdateItems.get(newItemPosition));
        }
    }

    public <S> void setOnItemBindListener(OnItemBindListener<S> onItemBindListener) {
        mOnItemBindListener = onItemBindListener;
    }

    public interface OnItemBindListener<S> {
        void itemBindListener(ViewDataBinding binding, S s);
    }

}
