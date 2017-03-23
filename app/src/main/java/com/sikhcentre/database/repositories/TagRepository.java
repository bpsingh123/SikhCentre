package com.sikhcentre.database.repositories;

import android.util.Log;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.TagDao;
import com.sikhcentre.entities.Topic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class TagRepository {
    private static final String TAG = "TopicRepository";

    private TagRepository() {
    }

    public static Set<Topic> getTopicSet(String txt) {
        try {
            List<Tag> authors = DbUtils.INSTANCE.getDaoSession().getTagDao().queryBuilder()
                    .where(TagDao.Properties.Name.like("%" + txt + "%"))
                    .list();
            Set<Topic> topics = new HashSet<>();
            for (Tag tag : authors) {
                topics.addAll(tag.getTopics());
            }
            return topics;

        } catch (Exception e) {
            Log.e(TAG, "getTopicSet:" + txt, e);
        }
        return new HashSet<>();
    }
}