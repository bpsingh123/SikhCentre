package com.sikhcentre.database.repositories;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.AuthorDao;
import com.sikhcentre.entities.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class AuthorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepository.class);

    private AuthorRepository() {
    }

    public static Set<Topic> getTopicSet(String txt) {
        try {
            List<Author> authors = getAuthorList(txt);
            Set<Topic> topics = new HashSet<>();
            for (Author author : authors) {
                topics.addAll(author.getTopics());
            }
            return topics;

        } catch (Exception e) {
            LOGGER.error("getTopicSet:" + txt, e);
        }
        return new HashSet<>();
    }

    public static List<Author> getAuthorList(String txt) {
        try {
            return DbUtils.INSTANCE.getDaoSession().getAuthorDao().queryBuilder()
                    .where(AuthorDao.Properties.Name.like("%" + txt + "%"))
                    .list();
        } catch (Exception e) {
            LOGGER.error("getAuthorList:" + txt, e);
        }
        return new ArrayList<>();
    }
}
