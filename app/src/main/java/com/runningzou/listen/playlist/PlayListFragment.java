package com.runningzou.listen.playlist;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;
import com.runningzou.listen.R;
import com.runningzou.listen.app.Injector;
import com.runningzou.listen.app.RxBus;
import com.runningzou.listen.base.list.BaseAdapter;
import com.runningzou.listen.base.list.FooterAdapter;
import com.runningzou.listen.base.rxjava.Live;
import com.runningzou.listen.databinding.FragmentPlayListBinding;
import com.runningzou.listen.databinding.ItemPlayListBinding;
import com.runningzou.listen.databinding.ItemPlayListFooterBinding;
import com.runningzou.listen.local.SongAddToPlayListEvent;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.player.PlayMode;
import com.runningzou.listen.ui.DefaultDividerDecoration;
import com.runningzou.listen.ui.PlayListDialog;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by zouzhihao on 2017/10/19.
 */

public class PlayListFragment extends Fragment implements Injector {


    @Inject
    PlayListViewModel mPlayListViewModel;

    private FragmentPlayListBinding mBinding;

    private PlayListAdapter mAdapter;

    private ItemPlayListFooterBinding mItemPlayListFooterBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list, container, false);

        mAdapter = new PlayListAdapter();
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addItemDecoration(new DefaultDividerDecoration());

        RxBus.getInstance().toObservable(SongAddToPlayListEvent.class)
                .compose(Live.<SongAddToPlayListEvent>bindLifecycle(this))
                .subscribe(new Consumer<SongAddToPlayListEvent>() {
                    @Override
                    public void accept(SongAddToPlayListEvent songAddToPlayListEvent) throws Exception {
                        mPlayListViewModel.addToPlayListRxCommand().execute(songAddToPlayListEvent);
                    }
                });

        mAdapter.setOnItemBindListener(new BaseAdapter.OnItemBindListener<PlayList>() {
            @Override
            public void itemBindListener(ViewDataBinding binding, final PlayList playList) {
                final ItemPlayListBinding bind = (ItemPlayListBinding) binding;
                RxView.clicks(bind.layoutAction)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {

                                PopupMenu actionMenu = new PopupMenu(getActivity(), bind.imageButtonAction, Gravity.END | Gravity.BOTTOM);
                                actionMenu.inflate(R.menu.play_list_action);

                                if (playList.getFavorite()) {
                                    actionMenu.getMenu().findItem(R.id.menu_item_rename).setVisible(false);
                                    actionMenu.getMenu().findItem(R.id.menu_item_delete).setVisible(false);
                                }

                                actionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {

                                        if (item.getItemId() == R.id.menu_item_delete) {
                                            mPlayListViewModel.deleteRxCommand().execute(playList);
                                        }

                                        if (item.getItemId() == R.id.menu_item_rename) {
                                            final PlayListDialog dialog = new PlayListDialog();
                                            dialog.setTitle(getResources().getString(R.string.listen_dialog_title_edit_play_list));
                                            dialog.show(getChildFragmentManager());

                                            dialog.setOnClickListener(new PlayListDialog.OnClickListener() {
                                                @Override
                                                public void onPositiveClick(String title) {
                                                    if (title != null && !title.equals("")) {
                                                        playList.setName(title);
                                                        mPlayListViewModel.updateRxCommand().execute(playList);
                                                    }
                                                    playList.setName(title);
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onNegativieClick() {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        if (item.getItemId() == R.id.menu_item_play_now) {

                                            if (playList != null && playList.getNumOfSongs() != 0) {
                                                RxBus.getInstance().post(playList);
                                            }
                                        }
                                        return true;
                                    }
                                });

                                actionMenu.show();
                            }
                        });
            }
        });


        mAdapter.setOnFooterBindListener(new FooterAdapter.OnFooterBindListener() {
            @Override
            public void onFooterBind(ViewDataBinding binding) {
                mItemPlayListFooterBinding = (ItemPlayListFooterBinding) binding;
                RxView.clicks(mItemPlayListFooterBinding.layoutAddPlayList)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                final PlayListDialog dialog = PlayListDialog.newInstance();
                                dialog.show(getChildFragmentManager());

                                dialog.setOnClickListener(new PlayListDialog.OnClickListener() {
                                    @Override
                                    public void onPositiveClick(String title) {
                                        if (title != null && !title.equals("")) {
                                            PlayList playList = new PlayList();
                                            playList.setName(title);
                                            playList.setCreatedAt(new Date());
                                            playList.setUpdatedAt(new Date());
                                            playList.setPlayMode(PlayMode.LIST);
                                            playList.setNumOfSongs(0);
                                            mPlayListViewModel.insertPlayListRxCommand().execute(playList);
                                        }
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegativieClick() {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });

        mPlayListViewModel
                .getPlayListRxCommand()
                .switchToLatest()
                .compose(Live.<List<PlayList>>bindLifecycle(this))
                .subscribe(new Consumer<List<PlayList>>() {
                    @Override
                    public void accept(List<PlayList> playLists) throws Exception {
                        mAdapter.refresh(playLists);
                    }
                });

        mPlayListViewModel
                .getPlayListRxCommand()
                .executing()
                .compose(Live.<Boolean>bindLifecycle(this))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            mBinding.progressBar.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.progressBar.setVisibility(View.GONE);
                        }

                    }
                });

        mPlayListViewModel
                .getPlayListRxCommand()
                .errors()
                .compose(Live.<Throwable>bindLifecycle(this))
                .subscribe(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        mPlayListViewModel
                .prepapreFavariteRxCommand(getContext())
                .switchToLatest()
                .compose(Live.<Boolean>bindLifecycle(this))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mPlayListViewModel.getPlayListRxCommand().execute(null);
                        }
                    }
                });

        mPlayListViewModel
                .playListCountRxCommand()
                .switchToLatest()
                .compose(Live.<Long>bindLifecycle(this))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mItemPlayListFooterBinding != null) {
                            String sum = getString(R.string.listen_play_list_footer_end_summary_formatter, aLong);
                            mItemPlayListFooterBinding.textViewSum.setText(sum);
                        }
                    }
                });

        mPlayListViewModel.
                insertPlayListRxCommand()
                .switchToLatest()
                .compose(Live.<PlayList>bindLifecycle(this))
                .subscribe(new Consumer<PlayList>() {
                    @Override
                    public void accept(PlayList playList) throws Exception {
                        mAdapter.loadMore(playList);
                        mPlayListViewModel.playListCountRxCommand().execute(null);
                    }
                });

        mPlayListViewModel
                .deleteRxCommand()
                .switchToLatest()
                .compose(Live.<PlayList>bindLifecycle(this))
                .subscribe(new Consumer<PlayList>() {
                    @Override
                    public void accept(PlayList playList) throws Exception {
                        mAdapter.deleteItem(playList);
                        mPlayListViewModel.playListCountRxCommand().execute(null);
                    }
                });

        mPlayListViewModel
                .updateRxCommand()
                .switchToLatest()
                .compose(Live.<PlayList>bindLifecycle(this))
                .subscribe(new Consumer<PlayList>() {
                    @Override
                    public void accept(PlayList playList) throws Exception {

                        int index = 0;
                        for (PlayList play : mAdapter.getItems()) {
                            if (play.getId() == playList.getId()) {
                                index = mAdapter.getItems().indexOf(play);
                            }
                        }

                        mAdapter.update(index);
                    }
                });

        mPlayListViewModel.addToPlayListRxCommand()
                .switchToLatest()
                .compose(Live.<PlayList>bindLifecycle(this))
                .subscribe(new Consumer<PlayList>() {
                    @Override
                    public void accept(PlayList list) throws Exception {

                        for (int i = 0; i < mAdapter.getItems().size(); i++) {
                            if (mAdapter.getItems().get(i).getId() == list.getId()) {
                                mAdapter.update(i);
                                break;
                            }
                        }

                    }
                });


        mPlayListViewModel.prepapreFavariteRxCommand(getContext()).execute(null);
        mPlayListViewModel.playListCountRxCommand().execute(null);

        return mBinding.getRoot();
    }


}
