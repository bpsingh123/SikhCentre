package com.sikhcentre.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 24/03/17.
 */
@Getter
@Setter
public class MediaPlayerServiceModel {
    private boolean playing;
    private int duration;

    public MediaPlayerServiceModel(boolean playing, int duration) {
        this.playing = playing;
        this.duration = duration;
    }
}
