package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.database.TopicRepository;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by brinder.singh on 28/01/17.
 */

public enum  SearchViewModel {
    INSTANCE;

    @NonNull
    private final BehaviorSubject<List<Topic>> topicList = BehaviorSubject.create();

    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    public void handleSearchTopic(String text) {
        topicList.onNext(TopicRepository.getTopicList(text));
    }

    public Observable<List<Topic>> getTopicListObservable() {
        return topicList.asObservable();
    }

}
