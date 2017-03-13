package com.sikhcentre.database;

import com.sikhcentre.entities.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class TopicRepository {
    public static List<Topic> getTopicList(String txt) {
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new Topic(1L, "Topic 1", Topic.TopicType.TEXT));
        topicList.add(new Topic(2L, "Topic 2", Topic.TopicType.TEXT));
        return topicList;
    }
}
