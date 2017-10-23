package com.runningzou.listen.base.list;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by zouzhihao on 2017/10/13.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public final ViewDataBinding binding;

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
