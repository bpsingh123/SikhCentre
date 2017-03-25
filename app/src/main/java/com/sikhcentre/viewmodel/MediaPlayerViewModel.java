package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by brinder.singh on 24/03/17.
 */

public enum MediaPlayerViewModel {
    INSTANCE;
    @NonNull

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaPlayerViewModel.class);
    private final BehaviorSubject<MediaPlayerModel> mediaPlayerModelBehaviorSubject = BehaviorSubject.create();
    private final BehaviorSubject<MediaPlayerServiceModel> mediaPlayerServiceModelBehaviorSubject = BehaviorSubject.create();

    public Observable<MediaPlayerModel> getMediaPlayerModelSubjectAsObservable() {
        return mediaPlayerModelBehaviorSubject;
    }

    public Observable<MediaPlayerServiceModel> getMediaPlayerServiceModelSubjectAsObservable() {
        return mediaPlayerServiceModelBehaviorSubject;
    }

    public void handlePlayerAction(MediaPlayerModel mediaPlayerModel) {
        mediaPlayerModelBehaviorSubject.onNext(mediaPlayerModel);
    }

    public void handlePlayerServiceAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        LOGGER.debug("handlePlayerServiceAction: ");
        mediaPlayerServiceModelBehaviorSubject.onNext(mediaPlayerServiceModel);
    }

}
