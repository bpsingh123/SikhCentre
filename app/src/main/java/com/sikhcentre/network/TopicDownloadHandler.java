package com.sikhcentre.network;

import android.util.Log;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicAuthor;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.models.Response;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brinder.singh on 21/03/17.
 */

public class TopicDownloadHandler {
    private static final String TAG = "TopicDownloadHandler";

    public void downloadJson() {

    }

    public void processTopicDownload(Response response) {
        List<Author> authors = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        List<TopicAuthor> topicAuthorList = new ArrayList<>();
        List<TopicTag> topicTagList = new ArrayList<>();
        List<Topic> topicList = new ArrayList<>();

        for (Response.AuthorResponse authorResponse : response.getAuthors()) {
            authors.add(new Author(authorResponse.getId(), authorResponse.getName()));
        }
        for (Response.TagResponse tagResponse : response.getTags()) {
            tags.add(new Tag(tagResponse.getId(), tagResponse.getName()));
        }
        for (Response.TopicResponse topicResponse : response.getTopics()) {
            topicList.add(new Topic(topicResponse.getId(), topicResponse.getTitle(),
                    topicResponse.getUrl(), topicResponse.getType()));
            for (Long id : topicResponse.getAuthorIds()) {
                topicAuthorList.add(new TopicAuthor(null, topicResponse.getId(), id));
            }
            for (Long id : topicResponse.getTagIds()) {
                topicTagList.add(new TopicTag(null, topicResponse.getId(), id));
            }
        }
        Database database = null;
        try {
            database = DbUtils.INSTANCE.getDaoSession().getDatabase();
            database.beginTransaction();
            DbUtils.INSTANCE.getDaoSession().getAuthorDao().insertInTx(authors);
            DbUtils.INSTANCE.getDaoSession().getTagDao().insertInTx(tags);
            DbUtils.INSTANCE.getDaoSession().getTopicDao().insertInTx(topicList);
            DbUtils.INSTANCE.getDaoSession().getTopicAuthorDao().insertInTx(topicAuthorList);
            DbUtils.INSTANCE.getDaoSession().getTopicTagDao().insertInTx(topicTagList);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Exception while writing to DB", e);
        } finally {
            DbUtils.endTransaction(database);
        }

    }
}
