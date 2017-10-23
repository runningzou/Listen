package com.runningzou.listen.ui;

import android.util.DisplayMetrics;

import com.runningzou.listen.app.ListenApp;


public class Utils {
    public static int dp2px(float dipValue) {
        final float scale = ListenApp.getApp().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = ListenApp.getApp().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;

    }
}