package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by brinder.singh on 24/03/17.
 */

public enum MediaPlayerViewModel {
    INSTANCE;
    @NonNull

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaPlayerViewModel.class);
    private final PublishSubject<MediaPlayerModel> mediaPlayerModelSubject = PublishSubject.create();
    private final PublishSubject<MediaPlayerServiceModel> mediaPlayerServiceModelSubject = PublishSubject.create();

    public Observable<MediaPlayerModel> getMediaPlayerModelSubjectAsObservable() {
        return mediaPlayerModelSubject;
    }

    public Observable<MediaPlayerServiceModel> getMediaPlayerServiceModelSubjectAsObservable() {
        return mediaPlayerServiceModelSubject;
    }

    public void handlePlayerAction(MediaPlayerModel mediaPlayerModel) {
        mediaPlayerModelSubject.onNext(mediaPlayerModel);
    }

    public void handlePlayerServiceAction(MediaPlayerServiceModel mediaPlayerServiceModel) {
        LOGGER.debug("handlePlayerServiceAction: ");
        mediaPlayerServiceModelSubject.onNext(mediaPlayerServiceModel);
    }

}
