package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.database.repositories.AuthorRepository;
import com.sikhcentre.database.repositories.TagRepository;
import com.sikhcentre.database.repositories.TopicRepository;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
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
import io.reactivex.subjects.PublishSubject;

/**
 * Created by brinder.singh on 28/01/17.
 */

public enum SearchViewModel {
    INSTANCE;

    @NonNull
    private final PublishSubject<List<Topic>> topicListSubject = PublishSubject.create();

    @NonNull
    private final PublishSubject<Topic> selectedTopicSubject = PublishSubject.create();

    private final PublishSubject<List<Author>> authorPublishSubject = PublishSubject.create();

    private final PublishSubject<List<Tag>> tagPublishSubject = PublishSubject.create();

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
        Set<Topic> topicSet = AuthorRepository.getTopicSet(text);
        topicSet.addAll(TagRepository.getTopicSet(text));
        topicSet.addAll(TopicRepository.getTopicList(text, true));

        //apply type filter
        topicSet = FilterViewModel.applyTypeFilterOnTopics(topicSet);

        //apply author filter
        topicSet = FilterViewModel.applyAuthorFilterOnTopics(topicSet);

        //apply tag filter
        topicSet = FilterViewModel.applyTagFilterOnTopics(topicSet);

        return new ArrayList<>(topicSet);
    }

    public Observable<List<Topic>> getTopicListSubjectAsObservable() {
        return topicListSubject;
    }

    public Observable<Topic> getSelectedTopicSubjectAsObservable() {
        return selectedTopicSubject;
    }

    public void handleSelectedTopic(Topic topic) {
        selectedTopicSubject.onNext(topic);
    }

    public Observable<List<Author>> getAuthorListSubjectAsObservable() {
        return authorPublishSubject;
    }

    public Observable<List<Tag>> getTagListSubjectAsObservable() {
        return tagPublishSubject;
    }

    public void handleSearchAuthor(final String text, final boolean applyFilter) {
        Observable.create(new ObservableOnSubscribe<List<Author>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Author>> e) throws Exception {
                e.onNext(AuthorRepository.getAuthorList(text, applyFilter));
            }
        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<List<Author>>() {
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
                    public void onNext(List<Author> authorList) {
                        authorPublishSubject.onNext(authorList);
                    }
                });

    }

    public void handleSearchTag(final String text, final boolean applyFilter) {
        Observable.create(new ObservableOnSubscribe<List<Tag>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Tag>> e) throws Exception {
                e.onNext(TagRepository.getTags(text, applyFilter));
            }
        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<List<Tag>>() {
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
                    public void onNext(List<Tag> tagList) {
                        tagPublishSubject.onNext(tagList);
                    }
                });

    }

}
