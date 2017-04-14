package com.sikhcentre.database.repositories;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicDao;
import com.sikhcentre.models.FilterSelectionModel;
import com.sikhcentre.utils.AppInfoProvider;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class TopicRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicRepository.class);

    private TopicRepository() {
    }

    public static List<Topic> getTopicList(String txt, boolean applyFilter) {
//        List<Topic> topicList = new ArrayList<>();
//        topicList.add(new Topic(1L, "ਰਾਜ਼ਸੀ ਬੁੱਧੀ", "", Topic.TopicType.TEXT));
//        topicList.add(new Topic(2L, "Topic 2", "", Topic.TopicType.IMAGE));
//        topicList.add(new Topic(3L, "Topic 3", "", Topic.TopicType.AUDIO));
//        topicList.add(new Topic(4L, "Topic 4", "", Topic.TopicType.VIDEO));
//        topicList.add(new Topic(5L, "Topic 5", "", Topic.TopicType.AUDIO));
//        topicList.add(new Topic(6L, "Topic 6", "", Topic.TopicType.VIDEO));
//        topicList.add(new Topic(7L, "Topic 7", "", Topic.TopicType.TEXT));

        try {
            QueryBuilder<Topic> qb = DbUtils.INSTANCE.getDaoSession().getTopicDao().queryBuilder();
            WhereCondition titleFilter = TopicDao.Properties.Title.like("%" + txt + "%");
            FilterSelectionModel filterSelectionModel = AppInfoProvider.INSTANCE.getFilterSelectionModel();
            if (applyFilter && !filterSelectionModel.getTopicTypeSet().isEmpty()) {
                WhereCondition typeFilter = TopicDao.Properties.TopicType.in(filterSelectionModel.getTopicTypeIdList());
                qb.where(titleFilter, typeFilter);
            } else {
                qb.where(titleFilter);
            }
            return qb.list();
        } catch (Exception e) {
            LOGGER.error("getTopicList:" + txt, e);
        }
        return new ArrayList<>();
    }
}
