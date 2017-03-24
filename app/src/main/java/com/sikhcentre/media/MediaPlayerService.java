package com.sikhcentre.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.viewmodel.MediaPlayerViewModel;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by brinder.singh on 19/03/17.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener {

    public static final String TAG = "MediaPlayerService";
    public static final String MEDIA_RESOURCE_KEY = "AUDIO_RESOURCE";
    public static final String ACTION_KEY = "ACTION";
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private CompositeSubscription subscription;
    private static boolean isServiceRunning = false;
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private MediaPlayerViewModel mediaPlayerViewModel;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        mediaPlayerViewModel = MediaPlayerViewModel.INSTANCE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        setServiceRunning(true);
        String url = intent.getStringExtra(MEDIA_RESOURCE_KEY);
        try {
            start(url);
        } catch (Exception e) {
            Log.e(TAG, "Exception while playing starting media:" + e.getMessage(), e);
            stop();
        }
        bind();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        unbind();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
    }

    private void bind() {
        subscription = new CompositeSubscription();
        subscription.add(mediaPlayerViewModel.getMediaPlayerModelSubjectAsObservable()
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<MediaPlayerModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MediaPlayerModel mediaPlayerModel) {
                        Log.d(TAG, "OnNext:" + mediaPlayerModel.getAction());
                        try {
                            switch (mediaPlayerModel.getAction()) {
                                case PLAY:
                                    play();
                                    break;
                                case STOP:
                                    stop();
                                    break;
                                case PAUSE:
                                    pause();
                                    break;
                                case CHECK_STATUS:
                                    mediaPlayerViewModel.handlePlayerServiceAction(getMediaPlayerInfo(mediaPlayer));
                                    break;
                                case CHANGE:
                                    start(mediaPlayerModel.getUrl());
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onNext: ", e);
                        }
                    }
                }));
    }

    private MediaPlayerServiceModel getMediaPlayerInfo(MediaPlayer mediaPlayer) {
        return new MediaPlayerServiceModel(mediaPlayer.isPlaying(), mediaPlayer.getDuration());
    }

    public void unbind() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void start(String url) throws IOException {
        if (url != null) {
            acquireWifiLock();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayerViewModel.handlePlayerServiceAction(getMediaPlayerInfo(mediaPlayer));
        play();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stop();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        releaseWifiLock();
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        releaseWifiLock();
        setServiceRunning(false);
        stopSelf();
    }

    private void acquireWifiLock() {
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mediaPlayerLock");
        wifiLock.acquire();
    }

    private void releaseWifiLock() {
        if (wifiLock != null && wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    public static void setServiceRunning(boolean isServiceRunning) {
        lock.writeLock().lock();
        MediaPlayerService.isServiceRunning = isServiceRunning;
        lock.writeLock().unlock();
    }

    public static boolean isServiceRunning() {
        lock.readLock().lock();
        try {
            return isServiceRunning;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static boolean startMediaPlayer(Context context, String url) {
        if (!isServiceRunning()) {
            Log.i(TAG, "Starting Media Player:" + url);
            Intent intent = new Intent(context, MediaPlayerService.class);
            intent.putExtra(MEDIA_RESOURCE_KEY, url);
            context.startService(intent);
            return true;
        } else {
            Log.i(TAG, "Media Player already running");
            return false;
        }
    }
}
