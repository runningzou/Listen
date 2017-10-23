package com.runningzou.listen.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.runningzou.listen.R;

/**
 * Created by zouzhihao on 2017/10/22.
 */

public class MaxHeightRecyclerView extends RecyclerView {
    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        int result = Utils.dp2px(getResources().getInteger(R.integer.listen_height_list_dialog));

        if (MeasureSpec.getSize(heightSpec) > result) {

            int newHeightSpec = MeasureSpec.makeMeasureSpec(result, MeasureSpec.getMode(heightSpec));
            super.onMeasure(widthSpec, newHeightSpec);
            return;
        }

        super.onMeasure(widthSpec, heightSpec);
    }
}
