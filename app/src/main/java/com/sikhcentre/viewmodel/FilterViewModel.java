package com.sikhcentre.viewmodel;

import com.sikhcentre.entities.Topic;
import com.sikhcentre.models.FilterModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
