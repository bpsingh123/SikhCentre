package com.sikhcentre.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sikhcentre.database.repositories.AuthorRepository;
import com.sikhcentre.database.repositories.TagRepository;
import com.sikhcentre.database.repositories.TopicRepository;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
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

    public void handleSearchTopic(final String text) {
        Observable.create(new Observable.OnSubscribe<List<Topic>>() {
            @Override
            public void call(Subscriber<? super List<Topic>> subscriber) {
                subscriber.onNext(getTopics(text));
            }
        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<List<Topic>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Topic> topics) {
                        topicListSubject.onNext(topics);
                    }
                });


    }

    private List<Topic> getTopics(String text) {
        Set<Topic> topics = AuthorRepository.getTopicSet(text);
        topics.addAll(TagRepository.getTopicSet(text));
        topics.addAll(TopicRepository.getTopicList(text));
        return new ArrayList<>(topics);
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
