package com.sikhcentre.database.repositories;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.TagDao;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class TagRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagRepository.class);

    private TagRepository() {
    }

    public static Set<Topic> getTopicSet(String txt) {
        try {
            List<Tag> tags = getTags(txt);
            Set<Topic> topics = new HashSet<>();
            for (Tag tag : tags) {
                for (TopicTag topicTag : tag.getTopicTags()) {
                    topics.add(topicTag.getTopic());
                }
            }
            return topics;
        } catch (Exception e) {
            LOGGER.error("getTopicSet:" + txt, e);
        }
        return new HashSet<>();
    }

    public static List<Tag> getTags(String txt) {
        try {
            return DbUtils.INSTANCE.getDaoSession().getTagDao().queryBuilder()
                    .where(TagDao.Properties.Name.like("%" + txt + "%"))
                    .list();

        } catch (Exception e) {
            LOGGER.error("getTags:" + txt, e);
        }
        return new ArrayList<>();
    }
}
