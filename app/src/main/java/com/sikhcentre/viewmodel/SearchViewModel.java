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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

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
        Observable.create(new ObservableOnSubscribe<List<Topic>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Topic>> e) throws Exception {
                e.onNext(getTopics(text));
            }
        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<List<Topic>>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

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
        return topicListSubject;
    }

    public Observable<Topic> getSelectedTopicSubjectAsObservable() {
        return selectedTopicSubject;
    }

    public void handleSelectedTopic(Context context, Topic topic) {
        selectedTopicSubject.onNext(topic);
    }

}
