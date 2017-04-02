package com.sikhcentre.models;

import com.sikhcentre.media.SikhCentreMediaPlayer.PlayerAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by brinder.singh on 24/03/17.
 */
@Getter
@Setter
public class MediaPlayerModel {
    private MediaPlayerModel(Builder builder) {
        setPlayerAction(builder.playerAction);
        setAction(builder.action);
        setSeekToTime(builder.seekToTime);
        setUrl(builder.url);
        setForwardTime(builder.forwardTime);
        setRewindTime(builder.rewindTime);
    }

    public enum Action {
        PLAY,
        PAUSE,
        STOP,
        SEEK,
        CHECK_STATUS,
        CHANGE,
        FORWARD,
        REWIND
    }

    private PlayerAction playerAction;
    private Action action;
    private int seekToTime;
    private String url;
    private int forwardTime;
    private int rewindTime;


    public static final class Builder {
        private PlayerAction playerAction;
        private Action action;
        private int seekToTime;
        private String url;
        private int forwardTime;
        private int rewindTime;

        public Builder() {
        }

        public Builder playerAction(PlayerAction val) {
            playerAction = val;
            return this;
        }

        public Builder action(Action val) {
            action = val;
            return this;
        }

        public Builder seekToTime(int val) {
            seekToTime = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder forwardTime(int val) {
            forwardTime = val;
            return this;
        }

        public Builder rewindTime(int val) {
            rewindTime = val;
            return this;
        }

        public MediaPlayerModel build() {
            return new MediaPlayerModel(this);
        }
    }
}
