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

import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.viewmodel.MediaPlayerViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


/**
 * Created by brinder.singh on 19/03/17.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaPlayerService.class.getSimpleName());
    public static final String MEDIA_RESOURCE_KEY = "AUDIO_RESOURCE";
    public static final String ACTION_KEY = "ACTION";
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private CompositeDisposable subscription;
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
        try {
            String url = intent.getStringExtra(MEDIA_RESOURCE_KEY);
            start(url);
        } catch (Exception e) {
            LOGGER.error("Exception while playing starting media:", e);
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
        unbind();
        subscription = new CompositeDisposable();
        subscription.add(mediaPlayerViewModel.getMediaPlayerModelSubjectAsObservable()
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Consumer<MediaPlayerModel>() {

                    @Override
                    public void accept(@NonNull MediaPlayerModel mediaPlayerModel) throws Exception {
                        LOGGER.debug("OnNext:{}", mediaPlayerModel.getAction());
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
                                case SEEK:
                                    handleSeek(mediaPlayerModel);
                                    break;
                            }
                        } catch (Exception e) {
                            LOGGER.error("onNext: ", e);
                        }
                    }

                }));
    }

    private MediaPlayerServiceModel getMediaPlayerInfo(MediaPlayer mediaPlayer) {
        return new MediaPlayerServiceModel.Builder()
                .playing(mediaPlayer.isPlaying())
                .duration(mediaPlayer.getDuration())
                .currentPosition(mediaPlayer.getCurrentPosition())
                .build();
    }

    private void handleSeek(MediaPlayerModel mediaPlayerModel){
        mediaPlayer.seekTo(mediaPlayerModel.getSeekToTime());
    }

    public void unbind() {
        if (subscription != null) {
            subscription.dispose();
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
    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                mediaPlayerViewModel.handlePlayerServiceAction(new MediaPlayerServiceModel
                        .Builder().isBuffering(true).build());
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mediaPlayerViewModel.handlePlayerServiceAction(new MediaPlayerServiceModel
                        .Builder().isBuffering(false).build());
                break;
        }
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        mediaPlayerViewModel.handlePlayerServiceAction(getMediaPlayerInfo(mediaPlayer));
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

    public static boolean startMediaPlayer(final Context context, final String url) {
        if (!isServiceRunning()) {
            io.reactivex.Observable.create(new ObservableOnSubscribe<Void>() {
                @Override
                public void subscribe(ObservableEmitter<Void> e) throws Exception {
                    LOGGER.info("Starting Media Player:{}", url);
                    Intent intent = new Intent(context, MediaPlayerService.class);
                    intent.putExtra(MEDIA_RESOURCE_KEY, url);
                    context.startService(intent);
                }
            }).subscribeOn(MainSchedulerProvider.INSTANCE.computation()).subscribe();
            return true;
        } else {
            LOGGER.info("Media Player already running");
            return false;
        }
    }
}
