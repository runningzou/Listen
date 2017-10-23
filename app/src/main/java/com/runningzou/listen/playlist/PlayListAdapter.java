package com.runningzou.listen.playlist;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.runningzou.listen.R;
import com.runningzou.listen.base.list.FooterAdapter;
import com.runningzou.listen.databinding.ItemPlayListBinding;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.ui.CharacterDrawable;

/**
 * Created by zouzhihao on 2017/10/20.
 */

public class PlayListAdapter extends FooterAdapter<PlayList> {

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
    }

    @Override
    protected int footerLayout() {
        return R.layout.item_play_list_footer;
    }

    @Override
    public void bindFooter(ViewDataBinding binding) {
    }

}
