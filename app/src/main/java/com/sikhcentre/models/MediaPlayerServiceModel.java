package com.sikhcentre.models;

import com.sikhcentre.media.SikhCentreMediaPlayer;

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
    private boolean isBuffering;
    private int currentPosition;
    private SikhCentreMediaPlayer.PlayerAction playerAction;

    private MediaPlayerServiceModel(Builder builder) {
        setPlaying(builder.playing);
        setDuration(builder.duration);
        setBuffering(builder.isBuffering);
        setCurrentPosition(builder.currentPosition);
        setPlayerAction(builder.playerAction);
    }


    public static final class Builder {
        private boolean playing;
        private int duration;
        private boolean isBuffering;
        private int currentPosition;
        private SikhCentreMediaPlayer.PlayerAction playerAction;

        public Builder() {
        }

        public Builder playing(boolean val) {
            playing = val;
            return this;
        }

        public Builder duration(int val) {
            duration = val;
            return this;
        }

        public Builder isBuffering(boolean val) {
            isBuffering = val;
            return this;
        }

        public Builder currentPosition(int val) {
            currentPosition = val;
            return this;
        }

        public Builder playerAction(SikhCentreMediaPlayer.PlayerAction val) {
            playerAction = val;
            return this;
        }

        public MediaPlayerServiceModel build() {
            return new MediaPlayerServiceModel(this);
        }
    }
}
