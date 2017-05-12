package com.sikhcentre.viewmodel;

import android.support.annotation.NonNull;

import com.sikhcentre.database.repositories.AuthorRepository;
import com.sikhcentre.database.repositories.TagRepository;
import com.sikhcentre.database.repositories.TopicRepository;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.models.IndexModel;
import com.sikhcentre.models.MetaDataResponse;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by brinder.singh on 15/04/17.
 */

public enum AddTopicViewModel {
    INSTANCE;
    @NonNull
    private final PublishSubject<IndexModel> indexModelPublishSubject = PublishSubject.create();

    @NonNull
    public PublishSubject<IndexModel> getIndexModelPublishSubject() {
        return indexModelPublishSubject;
    }

    public void handleLoadMetaResponse() {
        List<Topic> topicList = TopicRepository.getTopicList("", false);
        List<Tag> tagList = TagRepository.getTags("", false);
        List<Author> authorList = AuthorRepository.getAuthorList("", false);
        indexModelPublishSubject.onNext(new IndexModel(new MetaDataResponse(topicList, tagList, authorList)));
    }
}
