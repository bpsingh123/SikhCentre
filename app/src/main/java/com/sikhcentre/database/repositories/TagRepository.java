package com.sikhcentre.database.repositories;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.TagDao;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.viewmodel.FilterViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
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
            List<Tag> tags = getTags(txt, true);
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

    public static List<Tag> getTags(String txt, boolean applyFilter) {
        try {
            List<Tag> tagList = DbUtils.INSTANCE.getDaoSession().getTagDao().queryBuilder()
                    .where(TagDao.Properties.Name.like("%" + txt + "%"))
                    .list();

            if (applyFilter) {
                return FilterViewModel.filterTags(tagList);
            } else {
                return tagList;
            }

        } catch (Exception e) {
            LOGGER.error("getTags:" + txt, e);
        }
        return new ArrayList<>();
    }

    public static List<Tag> getTagListFromIds(Collection<Long> ids) {
        try {
            return DbUtils.INSTANCE.getDaoSession().getTagDao().queryBuilder()
                    .where(TagDao.Properties.Id.in(ids))
                    .list();
        } catch (Exception e) {
            LOGGER.error("getTagListFromIds:", e);
        }
        return new ArrayList<>();
    }
}
