package com.runningzou.listen.local;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.runningzou.listen.R;
import com.runningzou.listen.app.Injector;
import com.runningzou.listen.app.RxBus;
import com.runningzou.listen.base.list.BaseAdapter;
import com.runningzou.listen.base.rxjava.Live;
import com.runningzou.listen.databinding.DialogAddPlayListBinding;
import com.runningzou.listen.databinding.ItemPlayListBinding;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.Song;
import com.runningzou.listen.ui.BaseDialog;
import com.runningzou.listen.ui.CharacterDrawable;
import com.runningzou.listen.ui.DefaultDividerDecoration;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by zouzhihao on 2017/10/22.
 */

public class AddPlayListDialog extends BaseDialog implements Injector {

    public static final String TAG_SONG = "tag_song";

    public static final AddPlayListDialog newInstance(Song song) {
        AddPlayListDialog fragment = new AddPlayListDialog();
        Bundle args = new Bundle();
        args.putSerializable(TAG_SONG, song);
        fragment.setArguments(args);

        return fragment;
    }

    private PlayListAdapter mAdapter;

    private Song mSong;

    @Inject
    AddPlayListDialogViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mSong = (Song) args.getSerializable(TAG_SONG);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        //AndroidSupportInjection.inject(this);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_add_play_list;
    }

    @Override
    public void initView(ViewDataBinding binding) {

        final DialogAddPlayListBinding bind = (DialogAddPlayListBinding) binding;

        mAdapter = new PlayListAdapter();
        bind.recyclerView.addItemDecoration(new DefaultDividerDecoration());
        bind.recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemBindListener(new BaseAdapter.OnItemBindListener<PlayList>() {
            @Override
            public void itemBindListener(ViewDataBinding binding, final PlayList playList) {
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getInstance().post(new SongAddToPlayListEvent(mSong, playList));
                        dismiss();
                    }
                });
            }
        });

        mViewModel.getPlayListRxCommand().switchToLatest()
                .compose(Live.<List<PlayList>>bindLifecycle(this))
                .subscribe(new Consumer<List<PlayList>>() {
                    @Override
                    public void accept(List<PlayList> playLists) throws Exception {
                        mAdapter.refresh(playLists);
                    }
                });


        mViewModel.getPlayListRxCommand().execute(null);

        bind.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        int width = getResources().getInteger(R.integer.listen_width_dialog);
        setWidth(width);

    }

    public static class PlayListAdapter extends BaseAdapter<PlayList> {

        @Override
        protected int itemLayout() {
            return R.layout.item_play_list;
        }

        @Override
        protected void bindItem(ViewDataBinding binding, PlayList item) {
            ItemPlayListBinding bind = (ItemPlayListBinding) binding;

            if (item.getFavorite()) {
                bind.imageViewAlbum.setImageResource(R.drawable.ic_favorite_yes);
            } else {
                bind.imageViewAlbum.setImageDrawable(CharacterDrawable.generateAlbumDrawable(binding.getRoot().getContext(), item.getName()));
            }
            bind.textViewName.setText(item.getName());

            bind.textViewInfo.setText(binding.getRoot().getContext().getResources().getQuantityString(
                    R.plurals.listen_play_list_items_formatter, item.getNumOfSongs(), item.getNumOfSongs()));

            bind.layoutAction.setVisibility(View.GONE);
        }
    }


}
