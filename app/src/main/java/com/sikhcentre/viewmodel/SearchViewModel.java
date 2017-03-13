package com.sikhcentre.viewmodel;

import android.content.Context;
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

public enum SearchViewModel {
    INSTANCE;

    @NonNull
    private final BehaviorSubject<List<Topic>> topicListSubject = BehaviorSubject.create();

    @NonNull
    private final BehaviorSubject<Topic> selectedTopicSubject = BehaviorSubject.create();

    ISchedulerProvider schedulerProvider = MainSchedulerProvider.INSTANCE;

    public void handleSearchTopic(String text) {
        topicListSubject.onNext(TopicRepository.getTopicList(text));
    }

    public Observable<List<Topic>> getTopicListSubjectAsObservable() {
        return topicListSubject.asObservable();
    }

    public Observable<Topic> getSelectedTopicSubjectAsObservable() {
        return selectedTopicSubject.asObservable();
    }

    public void handleSelectedTopic(Context context, Topic topic) {
        selectedTopicSubject.onNext(topic);
    }

}
