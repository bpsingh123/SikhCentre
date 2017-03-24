package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.models.MediaPlayerModel;
import com.sikhcentre.models.MediaPlayerServiceModel;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by brinder.singh on 24/03/17.
 */

public enum MediaPlayerViewModel {
    INSTANCE;
    @NonNull

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
        mediaPlayerServiceModelBehaviorSubject.onNext(mediaPlayerServiceModel);
    }

}
