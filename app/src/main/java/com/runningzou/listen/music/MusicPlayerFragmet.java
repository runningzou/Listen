package com.runningzou.listen.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.runningzou.listen.R;
import com.runningzou.listen.app.Injector;
import com.runningzou.listen.app.RxBus;
import com.runningzou.listen.base.rxjava.Live;
import com.runningzou.listen.data.TimeUtils;
import com.runningzou.listen.databinding.FragmentMusicBinding;
import com.runningzou.listen.model.PlayList;
import com.runningzou.listen.model.Song;
import com.runningzou.listen.player.PlaybackService;
import com.runningzou.listen.player.Player;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by zouzhihao on 2017/10/19.
 */

public class MusicPlayerFragmet extends Fragment implements Injector {


    private PlaybackService mService;
    private FragmentMusicBinding mBinding;
    private boolean mProgressPause = false;
    private PlayList mPlayList;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((PlaybackService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private void updateUI(Song song) {
        if(mService.isPlaying()) {
            mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_pause);
        } else {
            mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_play);
        }
        mBinding.imageViewAlbum.startRotateAnimation();
        mBinding.textViewArtist.setText(song.getArtist());
        mBinding.textViewName.setText(song.getTitle());
        mBinding.textViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));

        if (song.getFavorite()) {
            mBinding.buttonFavoriteToggle.setImageResource(R.drawable.ic_favorite_yes);
        } else {
            mBinding.buttonFavoriteToggle.setImageResource(R.drawable.ic_favorite_no);
        }

//        Bitmap bitmap = AlbumUtils.parseAlbum(song);
//
//        Bitmap oldBitmap = ((BitmapDrawable) mBinding.imageViewAlbum.getDrawable()).getBitmap();
//        oldBitmap.recycle();
//
//        if (bitmap != null) {
//            mBinding.imageViewAlbum.setImageBitmap(AlbumUtils.getCroppedBitmap(bitmap));
//        } else {
//            mBinding.imageViewAlbum.setImageResource(R.drawable.default_record_album);
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().bindService(new Intent(getActivity(), PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music, container, false);

        RxBus.getInstance().toObservable(Song.class)
                .compose(Live.<Song>bindLifecycle(this))
                .subscribe(new Consumer<Song>() {
                    @Override
                    public void accept(Song song) throws Exception {
                        if (mService != null) {
                            mService.play(song);
                            mPlayList = mService.getPlayList();
                            updateUI(song);
                        }

                    }
                });

        RxBus.getInstance().toObservable(PlayList.class)
                .compose(Live.<PlayList>bindLifecycle(this))
                .subscribe(new Consumer<PlayList>() {
                    @Override
                    public void accept(PlayList playList) throws Exception {
                        if (mService != null) {

                            if (playList.prepare()) {
                                mService.play(playList);
                                mPlayList = playList;
                                updateUI(mService.getPlayingSong());
                            }

                        }

                    }
                });

        mBinding.buttonPlayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService == null) return;

                if (mPlayList == null) return;

                if (mService.isPlaying()) {
                    mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_play);
                    mBinding.imageViewAlbum.pauseRotateAnimation();
                    mService.pause();
                } else {
                    mBinding.buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                    mBinding.imageViewAlbum.resumeRotateAnimation();
                    mService.play();
                }
            }
        });

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBinding.textViewProgress.setText(TimeUtils.formatDuration(mService.getProgress()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mProgressPause = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int duration = getDuration(seekBar.getProgress());
                mService.seekTo(duration);
                mProgressPause = false;
            }
        });

        mBinding.buttonPlayLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPlayList == null) return;

                if (mService != null) {
                    mService.playLast();
                    updateUI(mService.getPlayingSong());
                }
            }
        });

        mBinding.buttonPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayList == null) return;

                if (mService != null) {
                    mService.playNext();
                    updateUI(mService.getPlayingSong());
                }
            }
        });

        mBinding.buttonPlayModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPlayList == null) return;

                mService.nextMode();

                switch (mService.getPlayMode()) {
                    case SINGLE:
                        mBinding.buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_single);
                        break;
                    case LOOP:
                        mBinding.buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_loop);
                        break;
                    case LIST:
                        mBinding.buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_list);
                        break;
                    case SHUFFLE:
                        mBinding.buttonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_shuffle);
                        break;
                }
            }
        });

        if (mService!= null && mService.isPlaying()) {
            updateUI(mService.getPlayingSong());
        }


        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Observable
                .interval(0, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(Live.<Long>bindLifecycle(this))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mService.isPlaying() && !mProgressPause) {
                            int progress = (int) (mBinding.seekBar.getMax() * ((float) mService.getProgress() / (float) getCurrentSongDuration()));
                            mBinding.textViewProgress.setText(TimeUtils.formatDuration(mService.getProgress()));
                            if (progress >= 0 && progress <= mBinding.seekBar.getMax()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    mBinding.seekBar.setProgress(progress, true);
                                } else {
                                    mBinding.seekBar.setProgress(progress);
                                }
                            }
                        }
                    }
                });

    }

    private int getCurrentSongDuration() {
        Song currentSong = mService.getPlayingSong();
        int duration = 0;
        if (currentSong != null) {
            duration = currentSong.getDuration();
        }
        return duration;
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / mBinding.seekBar.getMax()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }
}
