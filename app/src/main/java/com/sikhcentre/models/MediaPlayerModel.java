package com.sikhcentre.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 24/03/17.
 */
@Getter
@Setter
public class MediaPlayerModel {
    public enum Action {
        PLAY,
        PAUSE,
        STOP,
        SEEK,
        CHECK_STATUS,
        CHANGE
    }

    public MediaPlayerModel(Action action, int seekToTime) {
        this(action);
        this.seekToTime = seekToTime;
    }

    public MediaPlayerModel(Action action, String url) {
        this(action);
        this.url = url;
    }

    public MediaPlayerModel(Action action) {
        this.action = action;
    }


    private Action action;
    private int seekToTime;
    private String url;
}
