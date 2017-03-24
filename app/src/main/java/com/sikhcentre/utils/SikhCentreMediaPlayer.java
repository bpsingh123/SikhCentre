package com.sikhcentre.utils;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.media.MediaPlayerService;
import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.viewmodel.MediaPlayerViewModel;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

    enum Action {
        START,
        BUTTON_CLICK,
        SEEK_BAR_MOVED
    }

    private Action action;

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

    public void start(Topic topic) {
        action = Action.START;
        audioToolbar.setVisibility(View.VISIBLE);
        bind();

        if (!MediaPlayerService.startMediaPlayer(context, topic.getUrl()) && !topic.equals(this.topic)) {
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
        action = Action.BUTTON_CLICK;
        mediaPlayerViewModel.handlePlayerAction(new MediaPlayerModel(MediaPlayerModel.Action.CHECK_STATUS, 0));
    }

    public void bind() {
        compositeDisposable = new CompositeDisposable();

        Disposable disposable = mediaPlayerViewModel
                .getMediaPlayerServiceModelSubjectAsObservable()
                .observeOn(MainSchedulerProvider.INSTANCE.ui())
                .subscribe(new Consumer<MediaPlayerServiceModel>() {
                               @Override
                               public void accept(@NonNull MediaPlayerServiceModel mediaPlayerServiceModel) throws Exception {
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
                );

        compositeDisposable.add(disposable);
    }

    public void unbind() {
        if (compositeDisposable != null) {
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
    }
}
