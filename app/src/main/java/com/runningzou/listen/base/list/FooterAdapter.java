package com.runningzou.listen.base.list;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by zouzhihao on 2017/10/20.
 */

public abstract class FooterAdapter<T> extends BaseAdapter<T> {

    public int TYPE_FOOTER = 0x123;

    private OnFooterBindListener mOnFooterBindListener;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), footerLayout(), parent, false);
            return new BaseViewHolder(binding);
        }

        return super.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (getItemViewType(position) == TYPE_FOOTER) {
            if (mOnFooterBindListener != null) {
                mOnFooterBindListener.onFooterBind(holder.binding);
                bindFooter(holder.binding);
            }
        }
    }

    public abstract void bindFooter(ViewDataBinding binding);

    @Override
    public int getItemViewType(int position) {
        if (mItems.size() == 0) {
            return TYPE_FOOTER;
        }

        if (position == mItems.size()) {
            return TYPE_FOOTER;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mItems.size() == 0) {
            return 1;
        }

        return mItems.size() + 1;
    }

    public void setOnFooterBindListener(OnFooterBindListener onFooterBindListener) {
        mOnFooterBindListener = onFooterBindListener;
    }

    protected abstract int footerLayout();


    public interface OnFooterBindListener {
        void onFooterBind(ViewDataBinding binding);
    }
}
