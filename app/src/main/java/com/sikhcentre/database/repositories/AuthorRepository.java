package com.sikhcentre.database.repositories;

import android.util.Log;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.AuthorDao;
import com.sikhcentre.entities.Topic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class AuthorRepository {
    private static final String TAG = "TopicRepository";

    private AuthorRepository() {
    }

    public static Set<Topic> getTopicSet(String txt) {
        try {
            List<Author> authors = DbUtils.INSTANCE.getDaoSession().getAuthorDao().queryBuilder()
                    .where(AuthorDao.Properties.Name.like("%" + txt + "%"))
                    .list();
            Set<Topic> topics = new HashSet<>();
            for (Author author : authors) {
                topics.addAll(author.getTopics());
            }
            return topics;

        } catch (Exception e) {
            Log.e(TAG, "getTopicSet:" + txt, e);
        }
        return new HashSet<>();
    }
}
