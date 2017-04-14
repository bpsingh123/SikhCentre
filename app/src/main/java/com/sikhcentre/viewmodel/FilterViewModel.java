package com.sikhcentre.viewmodel;

import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.models.FilterModel;
import com.sikhcentre.utils.AppInfoProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brinder.singh on 08/04/17.
 */

public enum FilterViewModel {
    INSTANCE;
    private static List<FilterModel> filterModels;
    private static List<Topic.TopicType> topicTypeList = Arrays.asList(Topic.TopicType.values());

    static {
        filterModels = new ArrayList<>();
        filterModels.add(new FilterModel(FilterModel.FilterType.AUTHOR.toString(), true, FilterModel.FilterType.AUTHOR));
        filterModels.add(new FilterModel(FilterModel.FilterType.TYPE.toString(), false, FilterModel.FilterType.TYPE));
        filterModels.add(new FilterModel(FilterModel.FilterType.TAG.toString(), true, FilterModel.FilterType.TAG));
    }

    public static List<FilterModel> getFilterKeys() {
        return filterModels;
    }

    public static List<Topic.TopicType> getTopicTypeList() {
        return topicTypeList;
    }

    public static Set<Topic> applyTagFilterOnTopics(Set<Topic> topicSet) {
        //apply tag filter
        Set<Tag> tagSetFilter = AppInfoProvider.INSTANCE.getFilterSelectionModel().getTagSet();
        if (tagSetFilter.isEmpty()) {
            return topicSet;
        }
        Set<Topic> topicSetFiltered = new HashSet<>();
        for (Topic topic : topicSet) {
            Set<TopicTag> topicTagSet = new HashSet<>(topic.getTopicTags());
            Set<Tag> tagSet = new HashSet<>();
            for (TopicTag topicTag : topicTagSet) {
                tagSet.add(topicTag.getTag());
            }
            Set<Tag> tagSetCopy = new HashSet<>(tagSet);
            tagSetCopy.retainAll(tagSetFilter);
            if (!tagSetCopy.isEmpty()) {
                topicSetFiltered.add(topic);
            }
        }
        return topicSetFiltered;
    }

    public static Set<Topic> applyAuthorFilterOnTopics(Set<Topic> topicSet) {
        Set<Author> authorSetFilter = AppInfoProvider.INSTANCE.getFilterSelectionModel().getAuthorSet();
        if (authorSetFilter.isEmpty()) {
            return topicSet;
        }
        Set<Topic> topicSetFiltered = new HashSet<>();
        for (Topic topic : topicSet) {
            Set<Author> authorSet = new HashSet<>(topic.getAuthors());
            authorSet.retainAll(authorSetFilter);
            if (!authorSet.isEmpty()) {
                topicSetFiltered.add(topic);
            }
        }
        return topicSetFiltered;
    }

    public static Set<Topic> applyTypeFilterOnTopics(Set<Topic> topicSet) {
        Set<Topic.TopicType> topicTypeSetFilter = AppInfoProvider.INSTANCE.getFilterSelectionModel().getTopicTypeSet();
        if (topicTypeSetFilter.isEmpty()) {
            return topicSet;
        }
        Set<Topic> topicSetFiltered = new HashSet<>();
        for (Topic topic : topicSet) {
            if (topicTypeSetFilter.contains(topic.getTopicType())) {
                topicSetFiltered.add(topic);
            }
        }
        return topicSetFiltered;
    }

    public static List<Tag> filterTags(List<Tag> tagList) {
        Set<Tag> tagSet = AppInfoProvider.INSTANCE.getFilterSelectionModel().getTagSet();
        if (tagSet.isEmpty()) {
            return tagList;
        }
        List<Tag> tagListFiltered = new ArrayList<>();
        for (Tag tag : tagList) {
            if (tagSet.contains(tag)) {
                tagListFiltered.add(tag);
            }
        }
        return tagListFiltered;
    }

    public static List<Author> filterAuthors(List<Author> authorList) {
        Set<Author> authorSet = AppInfoProvider.INSTANCE.getFilterSelectionModel().getAuthorSet();
        if (authorSet.isEmpty()) {
            return authorList;
        }
        List<Author> authorListFiltered = new ArrayList<>();
        for (Author author : authorList) {
            if (authorSet.contains(author)) {
                authorListFiltered.add(author);
            }
        }
        return authorListFiltered;
    }

}
