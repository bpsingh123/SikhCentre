package com.sikhcentre.media;

import android.app.Activity;
import android.content.Context;
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
import com.sikhcentre.viewmodel.MediaPlayerViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by brinder.singh on 24/03/17.
 */

public class SikhCentreMediaPlayer implements View.OnClickListener {
    private Toolbar audioToolbar;
    private MediaPlayerViewModel mediaPlayerViewModel;
    private ImageView imageView;
    private SeekBar seekBar;
    private CompositeDisposable compositeDisposable;
    private Activity context;
    private TextView durationTV;
    private Topic topic;
    private static final Logger LOGGER = LoggerFactory.getLogger(SikhCentreMediaPlayer.class);


    enum Action {
        NONE,
        START,
        BUTTON_CLICK,
        SEEK_BAR_MOVED
    }

    private Action action = Action.NONE;

    public SikhCentreMediaPlayer(Activity context, int toolbarId) {
        this.context = context;

        mediaPlayerViewModel = MediaPlayerViewModel.INSTANCE;

        audioToolbar = (Toolbar) context.findViewById(toolbarId);
        imageView = (ImageView) context.findViewById(R.id.iv_play);
        seekBar = (SeekBar) context.findViewById(R.id.seekbar);
        durationTV = (TextView) context.findViewById(R.id.tv_time);

        imageView.setOnClickListener(this);
        audioToolbar.setVisibility(View.GONE);
    }

    public void start(Context context, Topic topic) {
        if(!NetworkUtils.isNetworkAvailable(context)){
            return;
        }
        LOGGER.debug("start: {}", topic);
        action = Action.START;
        audioToolbar.setVisibility(View.VISIBLE);
        bind();

        if (!MediaPlayerService.startMediaPlayer(context, topic.getUrl()) && !topic.equals(this.topic)) {
            LOGGER.debug("start through event");
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.CHANGE, topic.getUrl()));
        }

        this.topic = topic;
    }

    public void stop() {
        mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.STOP));
        audioToolbar.setVisibility(View.GONE);
        unbind();
    }

    @Override
    public void onClick(View view) {
        LOGGER.debug("onClick");
        action = Action.BUTTON_CLICK;
        mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.CHECK_STATUS, 0));
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
                                   LOGGER.debug("OnNext:{}", action);
                                   switch (action) {
                                       case BUTTON_CLICK:
                                           handleButtonClickAction(mediaPlayerServiceModel);
                                           break;
                                       case START:
                                           handleStartAction(mediaPlayerServiceModel);
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
        }
    }

    private void handleButtonClickAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        if (mediaPlayerServiceModel.isPlaying()) {
            imageView.setImageResource(R.drawable.ic_play_circle_outline_blue_900_24dp);
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.PAUSE));
        } else {
            imageView.setImageResource(R.drawable.ic_pause_circle_outline_blue_900_24dp);
            mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.PLAY));
        }
    }

    private void handleStartAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        int mins = mediaPlayerServiceModel.getDuration() / (1000 * 60);
        int hours = mins / 60;
        mins = mins % 60;
        String time = String.format("%2d:%2d", hours, mins);
        durationTV.setText(time);
        imageView.setImageResource(R.drawable.ic_pause_circle_outline_blue_900_24dp);
    }
}
