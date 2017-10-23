package com.runningzou.listen.main;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.runningzou.listen.R;
import com.runningzou.listen.app.Injector;
import com.runningzou.listen.base.rxjava.Live;
import com.runningzou.listen.databinding.ActivityMainBinding;
import com.runningzou.listen.local.LocalFilesFragment;
import com.runningzou.listen.music.MusicPlayerFragmet;
import com.runningzou.listen.playlist.PlayListFragment;
import com.runningzou.listen.settings.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, Injector {

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    public static final int DEFAULT_PAGE_INDEX = 2;

    public static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    private String[] mTitles;

    private Fragment[] mFragments;

    private List<RadioButton> mRadioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mRadioButtons = new ArrayList<>();
        mRadioButtons.add(mBinding.include.radioButtonPlayList);
        mRadioButtons.add(mBinding.include.radioButtonMusic);
        mRadioButtons.add(mBinding.include.radioButtonLocalFiles);
        //mRadioButtons.add(mBinding.include.radioButtonSettings);

        RxRadioGroup.checkedChanges(mBinding.include.radioGroup)
                .compose(Live.<Integer>bindLifecycle(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        int position = 0;
                        switch (integer) {
                            case R.id.radio_button_play_list:
                                position = 0;
                                break;
                            case R.id.radio_button_music:
                                position = 1;
                                break;
                            case R.id.radio_button_local_files:
                                position = 2;
                                break;
//                            case R.id.radio_button_settings:
//                                position = 3;
//                                break;
                        }

                        mRadioButtons.get(position).setChecked(true);
                        mBinding.toolbar.setTitle(mTitles[position]);
                        mBinding.viewPager.setCurrentItem(position);

                    }
                });

        mTitles = getResources().getStringArray(R.array.listen_main_titles);

        mFragments = new Fragment[mTitles.length];

        mFragments[0] = new PlayListFragment();
        mFragments[1] = new MusicPlayerFragmet();
        mFragments[2] = new LocalFilesFragment();
        //mFragments[3] = new SettingFragment();

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);

        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        mBinding.viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.listen_margin_large));
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRadioButtons.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRadioButtons.get(DEFAULT_PAGE_INDEX).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }
}
