package com.sikhcentre.database.repositories;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.AuthorDao;
import com.sikhcentre.entities.Topic;
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

public class AuthorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepository.class);

    private AuthorRepository() {
    }

    public static Set<Topic> getTopicSet(String txt) {
        try {
            List<Author> authors = getAuthorList(txt, true);
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

    public static List<Author> getAuthorList(String txt, boolean applyFilter) {
        try {
            List<Author> authorList = DbUtils.INSTANCE.getDaoSession().getAuthorDao().queryBuilder()
                    .where(AuthorDao.Properties.Name.like("%" + txt + "%"))
                    .list();
            if (applyFilter) {
                return FilterViewModel.filterAuthors(authorList);
            } else {
                return authorList;
            }
        } catch (Exception e) {
            LOGGER.error("getAuthorList:" + txt, e);
        }
        return new ArrayList<>();
    }

    public static Collection<Author> getAuthorListFromIds(Collection<Long> ids) {
        try {
            return DbUtils.INSTANCE.getDaoSession().getAuthorDao().queryBuilder()
                    .where(AuthorDao.Properties.Id.in(ids))
                    .list();
        } catch (Exception e) {
            LOGGER.error("getAuthorListFromIds:", e);
        }
        return new ArrayList<>();
    }
}
