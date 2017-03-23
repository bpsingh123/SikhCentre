package com.sikhcentre.database.repositories;

import android.util.Log;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class TopicRepository {
    private static final String TAG = "TopicRepository";

    private TopicRepository() {
    }

    public static List<Topic> getTopicList(String txt) {
//        List<Topic> topicList = new ArrayList<>();
//        topicList.add(new Topic(1L, "ਰਾਜ਼ਸੀ ਬੁੱਧੀ", "", Topic.TopicType.TEXT));
//        topicList.add(new Topic(2L, "Topic 2", "", Topic.TopicType.IMAGE));
//        topicList.add(new Topic(3L, "Topic 3", "", Topic.TopicType.AUDIO));
//        topicList.add(new Topic(4L, "Topic 4", "", Topic.TopicType.VIDEO));
//        topicList.add(new Topic(5L, "Topic 5", "", Topic.TopicType.AUDIO));
//        topicList.add(new Topic(6L, "Topic 6", "", Topic.TopicType.VIDEO));
//        topicList.add(new Topic(7L, "Topic 7", "", Topic.TopicType.TEXT));

        try {
            return DbUtils.INSTANCE.getDaoSession().getTopicDao().queryBuilder()
                    .where(TopicDao.Properties.Title.like("%" + txt + "%"))
                    .list();
        } catch (Exception e) {
            Log.e(TAG, "getTopicList:" + txt, e);
        }
        return new ArrayList<>();
    }
}
