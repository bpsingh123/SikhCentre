package com.sikhcentre.models;

import com.google.gson.Gson;
import com.sikhcentre.database.repositories.AuthorRepository;
import com.sikhcentre.database.repositories.TagRepository;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

/**
 * Created by brinder.singh on 14/04/17.
 */
@Getter
public class FilterSelectionModel {
    private Set<Author> authorSet;
    private Set<Tag> tagSet;
    private Set<Topic.TopicType> topicTypeSet;

    public FilterSelectionModel() {
        authorSet = new HashSet<>();
        tagSet = new HashSet<>();
        topicTypeSet = new HashSet<>();
    }

    public FilterSelectionModel(Set<Author> authorSet, Set<Tag> tagSet, Set<Topic.TopicType> topicTypeSet) {
        this.authorSet = authorSet;
        this.tagSet = tagSet;
        this.topicTypeSet = topicTypeSet;
    }

    public List<Integer> getTopicTypeIdList() {
        List<Integer> topicTypeIdList = new ArrayList<>();
        for (Topic.TopicType topicType : topicTypeSet) {
            topicTypeIdList.add(topicType.getId());
        }
        return topicTypeIdList;
    }

    public String toJSONString() {
        return new Gson().toJson(getFilterSelectionModelSerializableFromFilterSelectionModel(this));
    }

    public static FilterSelectionModel fromJSON(String json) {
        if (json == null) {
            return new FilterSelectionModel();
        }
        return getFilterSelectionModelFromFilterSelectionModelSerializable(new Gson()
                .fromJson(json, FilterSelectionModelSerializable.class));
    }

    @Getter
    public static class FilterSelectionModelSerializable {
        private List<Long> authorIdList;
        private List<Long> tagIdList;
        private List<Integer> topicTypeIdList;

        public FilterSelectionModelSerializable(List<Long> authorIdList, List<Long> tagIdList, List<Integer> topicTypeIdList) {
            this.authorIdList = authorIdList;
            this.tagIdList = tagIdList;
            this.topicTypeIdList = topicTypeIdList;
        }
    }

    private static FilterSelectionModelSerializable getFilterSelectionModelSerializableFromFilterSelectionModel(
            FilterSelectionModel filterSelectionModel) {
        List<Long> authorIdList = new ArrayList<>();
        List<Long> tagIdList = new ArrayList<>();
        List<Integer> topicTypeIdList = new ArrayList<>();
        for (Author author : filterSelectionModel.getAuthorSet()) {
            authorIdList.add(author.getId());
        }
        for (Tag tag : filterSelectionModel.getTagSet()) {
            tagIdList.add(tag.getId());
        }
        for (Topic.TopicType topicType : filterSelectionModel.getTopicTypeSet()) {
            topicTypeIdList.add(topicType.ordinal());
        }
        return new FilterSelectionModelSerializable(authorIdList, tagIdList, topicTypeIdList);
    }

    private static FilterSelectionModel getFilterSelectionModelFromFilterSelectionModelSerializable(FilterSelectionModelSerializable filterSelectionModelSerializable) {
        Set<Author> authorSet = new HashSet<>(AuthorRepository.getAuthorListFromIds(filterSelectionModelSerializable.getAuthorIdList()));
        Set<Tag> tagSet = new HashSet<>(TagRepository.getTagListFromIds(filterSelectionModelSerializable.getTagIdList()));
        Set<Topic.TopicType> topicTypeSet = new HashSet<>();

        Topic.TopicType[] topicValues = Topic.TopicType.values();
        for (Integer topicTypeInteger : filterSelectionModelSerializable.getTopicTypeIdList()) {
            topicTypeSet.add(topicValues[topicTypeInteger]);
        }

        return new FilterSelectionModel(authorSet, tagSet, topicTypeSet);
    }
}
