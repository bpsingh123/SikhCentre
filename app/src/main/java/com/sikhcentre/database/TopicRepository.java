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
        topicList.add(new Topic(3L, "Topic 3", Topic.TopicType.TEXT));
        topicList.add(new Topic(4L, "Topic 4", Topic.TopicType.TEXT));
        topicList.add(new Topic(5L, "Topic 5", Topic.TopicType.TEXT));
        topicList.add(new Topic(6L, "Topic 6", Topic.TopicType.TEXT));
        topicList.add(new Topic(7L, "Topic 7", Topic.TopicType.TEXT));
        return topicList;
    }
}
