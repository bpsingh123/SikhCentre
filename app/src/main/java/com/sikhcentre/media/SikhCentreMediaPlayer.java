package com.sikhcentre.media;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.NetworkUtils;
import com.sikhcentre.utils.UIUtils;
import com.sikhcentre.viewmodel.MediaPlayerViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.sikhcentre.media.SikhCentreMediaPlayer.PlayerAction.BUTTON_CLICK;
import static com.sikhcentre.media.SikhCentreMediaPlayer.PlayerAction.CHECK_STATUS;
import static com.sikhcentre.media.SikhCentreMediaPlayer.PlayerAction.SEEK_BAR_MOVED;

/**
 * Created by brinder.singh on 24/03/17.
 */

public class SikhCentreMediaPlayer implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final ImageView rewindButton;
    private final ImageView forwardButton;
    private Toolbar audioToolbar;
    private MediaPlayerViewModel mediaPlayerViewModel;
    private ImageView playPauseButton;
    private SeekBar seekBar;
    private CompositeDisposable compositeDisposable;
    private Activity context;
    private TextView durationTV;
    private Topic topic;
    private static final Logger LOGGER = LoggerFactory.getLogger(SikhCentreMediaPlayer.class);
    private ProgressDialog progressDialog;
    private Disposable getTimingDisposable;

    public enum PlayerAction {
        START,
        BUTTON_CLICK,
        CHECK_STATUS,
        SEEK_BAR_MOVED,
        STOPPED
    }

    public SikhCentreMediaPlayer(Activity context, int toolbarId) {
        this.context = context;

        mediaPlayerViewModel = MediaPlayerViewModel.INSTANCE;

        audioToolbar = (Toolbar) context.findViewById(toolbarId);
        playPauseButton = (ImageView) context.findViewById(R.id.iv_play);
        rewindButton = (ImageView) context.findViewById(R.id.iv_rewind);
        forwardButton = (ImageView) context.findViewById(R.id.iv_forward);
        seekBar = (SeekBar) context.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setEnabled(false);
        durationTV = (TextView) context.findViewById(R.id.tv_time);

        playPauseButton.setOnClickListener(this);
        audioToolbar.setVisibility(View.GONE);

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                        .action(MediaPlayerModel.Action.FORWARD)
                        .forwardTime(5000)
                        .playerAction(SEEK_BAR_MOVED)
                        .build());
                showBufferingDialog();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                        .action(MediaPlayerModel.Action.REWIND)
                        .rewindTime(5000)
                        .playerAction(SEEK_BAR_MOVED)
                        .build());
                showBufferingDialog();
            }
        });
    }

    public void start(Topic topic) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return;
        }
        LOGGER.debug("start: {}", topic);
        audioToolbar.setVisibility(View.VISIBLE);
        bind();

        boolean isMediaPlayerServiceStarted = MediaPlayerService.startMediaPlayer(context, topic.getUrl());

        if (!isMediaPlayerServiceStarted && !topic.equals(this.topic)) {
            LOGGER.debug("start through event");
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                    .playerAction(PlayerAction.START)
                    .action(MediaPlayerModel.Action.CHANGE)
                    .url(topic.getUrl())
                    .build());
            isMediaPlayerServiceStarted = true;
        }

        if (isMediaPlayerServiceStarted) {
            showBufferingDialog();
        }
        this.topic = topic;
    }

    private void showBufferingDialog() {
        UIUtils.dismissProgressBar(progressDialog);
        progressDialog = UIUtils.showProgressBar(context,
                context.getString(R.string.loading_indicator_title),
                context.getString(R.string.loading_indicator_buffering));
    }

    public void stop() {
        mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                .action(MediaPlayerModel.Action.STOP)
                .build());
        audioToolbar.setVisibility(View.GONE);
        unbind();
    }

    @Override
    public void onClick(View view) {
        LOGGER.debug("onClick");
        if (MediaPlayerService.isServiceRunning()) {
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                    .playerAction(BUTTON_CLICK)
                    .action(MediaPlayerModel.Action.CHECK_STATUS)
                    .seekToTime(0)
                    .build());
        } else {
            start(topic);
        }
    }

    public void bind() {
        LOGGER.debug("bind");
        unbind();
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(mediaPlayerViewModel
                .getMediaPlayerServiceModelSubjectAsObservable()
                .observeOn(MainSchedulerProvider.INSTANCE.ui())
                .subscribe(new Consumer<MediaPlayerServiceModel>() {
                               @Override
                               public void accept(@NonNull MediaPlayerServiceModel mediaPlayerServiceModel) throws Exception {
                                   LOGGER.debug("brinder OnNext:{}", mediaPlayerServiceModel.getPlayerAction());
                                   switch (mediaPlayerServiceModel.getPlayerAction()) {
                                       case BUTTON_CLICK:
                                           handleButtonClickAction(mediaPlayerServiceModel);
                                           break;
                                       case START:
                                           handleStartAction(mediaPlayerServiceModel);
                                           break;
                                       case CHECK_STATUS:
                                           handleGetTimingAction(mediaPlayerServiceModel);
                                           break;
                                       case SEEK_BAR_MOVED:
                                           handleSeekBarMovedAction();
                                           break;
                                       case STOPPED:
                                           handleStoppedAction(mediaPlayerServiceModel);
                                           break;
                                   }
                               }
                           }
                ));
    }

    public void unbind() {
        LOGGER.debug("unbind");
        if (compositeDisposable != null) {
            LOGGER.debug("desposing");
            compositeDisposable.dispose();
            stopGetTimingDisposable();
        }
    }

    private void handleButtonClickAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        if (mediaPlayerServiceModel.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.ic_play_circle_outline_blue_900_24dp);
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                    .action(MediaPlayerModel.Action.PAUSE)
                    .playerAction(CHECK_STATUS)
                    .build());
            stopGetTimingDisposable();
        } else {
            playPauseButton.setImageResource(R.drawable.ic_pause_circle_outline_blue_900_24dp);
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                    .action(MediaPlayerModel.Action.PLAY)
                    .playerAction(CHECK_STATUS)
                    .build());
            startGetTimingDisposable();
        }
    }

    private void handleStartAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        UIUtils.dismissProgressBar(progressDialog);
        seekBar.setEnabled(true);
        seekBar.setMax(mediaPlayerServiceModel.getDuration());
        handleGetTimingAction(mediaPlayerServiceModel);
        playPauseButton.setImageResource(R.drawable.ic_pause_circle_outline_blue_900_24dp);
        startGetTimingDisposable();
    }

    private void startGetTimingDisposable() {
        stopGetTimingDisposable();
        LOGGER.debug("brinder starting getTimingDisposable");
        getTimingDisposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        LOGGER.debug("brinder get time");
                        mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                                .action(MediaPlayerModel.Action.CHECK_STATUS)
                                .playerAction(CHECK_STATUS)
                                .build());
                    }
                });
    }

    private void stopGetTimingDisposable() {
        if (getTimingDisposable != null) {
            LOGGER.debug("brinder disposing getTimingDisposable");
            getTimingDisposable.dispose();
        }
    }

    private void handleGetTimingAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        LOGGER.debug("brinder setting time");
        seekBar.setProgress(mediaPlayerServiceModel.getCurrentPosition());
        String totalTime = convertMillisToTimeString(mediaPlayerServiceModel.getDuration());
        String pendingTime = convertMillisToTimeString(mediaPlayerServiceModel.getCurrentPosition());
        durationTV.setText(pendingTime + "/" + totalTime);
    }

    private void handleBuferingUpdate(MediaPlayerServiceModel mediaPlayerServiceModel) {
    }

    private String convertMillisToTimeString(int millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            LOGGER.info("Brinder Seek Bar Moved");
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel.Builder()
                    .action(MediaPlayerModel.Action.SEEK)
                    .seekToTime(progress)
                    .playerAction(SEEK_BAR_MOVED)
                    .build());
            showBufferingDialog();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void handleSeekBarMovedAction() {
        LOGGER.info("Brinder Seek Bar Moved Complete");
        UIUtils.dismissProgressBar(progressDialog);
    }

    private void handleStoppedAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        LOGGER.info("Brinder Stopped");
        seekBar.setProgress(0);
        seekBar.setEnabled(false);
        playPauseButton.setImageResource(R.drawable.ic_play_circle_outline_blue_900_24dp);
        handleGetTimingAction(mediaPlayerServiceModel);
    }

}
