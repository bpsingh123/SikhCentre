package com.sikhcentre.database.repositories;

import android.util.Log;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.AuthorDao;
import com.sikhcentre.entities.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class AuthorRepository {
    private static final String TAG = "TopicRepository";

    public static List<Topic> getTopicList(String txt) {
        try {
             List<Author> authors = DbUtils.INSTANCE.getDaoSession().getAuthorDao().queryBuilder()
                    .where(AuthorDao.Properties.Name.like("%" + txt + "%"))
                    .listLazy();
        } catch (Exception e) {
            Log.e(TAG, "getTopicList:" + txt, e);
        }
        return new ArrayList<>();
    }
}
