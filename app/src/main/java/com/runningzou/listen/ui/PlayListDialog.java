package com.runningzou.listen.ui;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.runningzou.listen.R;
import com.runningzou.listen.databinding.DialogEditPlayListBinding;

import io.reactivex.functions.Consumer;

/**
 * Created by zouzhihao on 2017/10/21.
 */

public class PlayListDialog extends BaseDialog {

    private String mTitle;
    private String mHint;

    private OnClickListener mOnClickListener;

    public static PlayListDialog newInstance() {
        return new PlayListDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int width = getResources().getInteger(R.integer.listen_width_dialog);
        setWidth(width);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_edit_play_list;
    }

    @Override
    public void initView(ViewDataBinding binding) {
        final DialogEditPlayListBinding bind = (DialogEditPlayListBinding) binding;

        if (mOnClickListener != null) {
            RxView.clicks(bind.btnCancle).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    mOnClickListener.onNegativieClick();
                }
            });

            RxView.clicks(bind.btnSure).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    String title = bind.editTxt.getText().toString();
                    mOnClickListener.onPositiveClick(title);
                }
            });

        }

        if (mTitle != null && !mTitle.equals("")) {
            bind.txtTitle.setText(mTitle);
        }

        if (mHint != null && !mHint.equals("")) {
            bind.editTxt.setHint(mHint);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onPositiveClick(String title);

        void onNegativieClick();
    }


    public void setTitle(String title) {
        mTitle = title;
    }

    public void setHint(String hint) {
        mHint = hint;
    }
}
