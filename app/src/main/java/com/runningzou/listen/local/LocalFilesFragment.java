package com.runningzou.listen.local;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runningzou.listen.R;
import com.runningzou.listen.app.Injector;
import com.runningzou.listen.app.RxBus;
import com.runningzou.listen.base.list.BaseAdapter;
import com.runningzou.listen.base.list.FooterAdapter;
import com.runningzou.listen.base.rxjava.Live;
import com.runningzou.listen.databinding.FragmentLocalFileBinding;
import com.runningzou.listen.databinding.ItemLocalMusicBinding;
import com.runningzou.listen.databinding.ItemLocalMusicFooterBinding;
import com.runningzou.listen.model.Song;
import com.runningzou.listen.ui.DefaultDividerDecoration;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.functions.Consumer;

/**
 * Created by zouzhihao on 2017/10/19.
 */

public class LocalFilesFragment extends Fragment implements HasSupportFragmentInjector, Injector {

    private LocalMusicAdapter mAdapter;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Inject
    LocalFileViewModel mViewModel;

    private FragmentLocalFileBinding mBinding;

    private ItemLocalMusicFooterBinding mFooterBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_file, container, false);

        mAdapter = new LocalMusicAdapter();

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DefaultDividerDecoration());

        mAdapter.setOnFooterBindListener(new FooterAdapter.OnFooterBindListener() {
            @Override
            public void onFooterBind(ViewDataBinding binding) {
                mFooterBinding = (ItemLocalMusicFooterBinding) binding;
            }
        });

        mAdapter.setOnItemBindListener(new BaseAdapter.OnItemBindListener<Song>() {
            @Override
            public void itemBindListener(ViewDataBinding binding, final Song song) {
                final ItemLocalMusicBinding bind = (ItemLocalMusicBinding) binding;
                bind.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getInstance().post(song);
                    }
                });

                bind.layoutAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddPlayListDialog.newInstance(song).show(getChildFragmentManager());
                    }
                });

            }
        });

        mBinding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.listen_theme_dark_blue_colorPrimary));
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.localSongsRxCommand().execute(null);
            }
        });

        mViewModel.localSongsRxCommand()
                .switchToLatest()
                .compose(Live.<List<Song>>bindLifecycle(this))
                .subscribe(new Consumer<List<Song>>() {
                    @Override
                    public void accept(final List<Song> songs) throws Exception {
                        if (songs == null || songs.size() == 0) {
                            mBinding.recyclerView.setVisibility(View.GONE);
                            mBinding.txtNoSongs.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (mFooterBinding != null) {
                            mFooterBinding.textViewSum.setText(getString(R.string.listen_local_footer_end_summary_formatter, songs.size()));
                        }
                        mAdapter.refresh(songs);
                        mBinding.swipeRefresh.setRefreshing(false);
                    }
                });

        mViewModel.localSongsRxCommand()
                .executing()
                .compose(Live.<Boolean>bindLifecycle(this))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                });

        mViewModel.localSongsRxCommand().execute(null);

        return mBinding.getRoot();
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }
}
