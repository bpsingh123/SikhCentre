package com.sikhcentre.network;

import com.google.gson.Gson;
import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicAuthor;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.models.Response;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.StringUtils;

import org.greenrobot.greendao.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.content.ContentValues.TAG;

/**
 * Created by brinder.singh on 21/03/17.
 */

public class TopicMetadataDownloadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMetadataDownloadHandler.class);

    private static Response downloadJson() {
        try {
            URL url = new URL("https://dl.dropboxusercontent.com/s/b0puce7rrpxkpjh/response.json");
            String str = StringUtils.toString(url.openStream());
            return new Gson().fromJson(str, Response.class);
        } catch (MalformedURLException e) {
            LOGGER.error("Error while reading url", e);
        } catch (IOException e) {
            LOGGER.error(TAG, "Error while reading file", e);
        } catch (Exception e) {
            LOGGER.error(TAG, "Error while populating data from URL", e);
        }
        return new Response();
    }

    private static void processTopicDownload(Response response) {
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
        if (!topicList.isEmpty()) {
            Database database = null;
            try {
                database = DbUtils.INSTANCE.getDaoSession().getDatabase();
                database.beginTransaction();

                DbUtils.INSTANCE.getDaoSession().getAuthorDao().deleteAll();
                DbUtils.INSTANCE.getDaoSession().getTagDao().deleteAll();
                DbUtils.INSTANCE.getDaoSession().getTopicDao().deleteAll();
                DbUtils.INSTANCE.getDaoSession().getTopicAuthorDao().deleteAll();
                DbUtils.INSTANCE.getDaoSession().getTopicTagDao().deleteAll();

                DbUtils.INSTANCE.getDaoSession().getAuthorDao().insertInTx(authors);
                DbUtils.INSTANCE.getDaoSession().getTagDao().insertInTx(tags);
                DbUtils.INSTANCE.getDaoSession().getTopicDao().insertInTx(topicList);
                DbUtils.INSTANCE.getDaoSession().getTopicAuthorDao().insertInTx(topicAuthorList);
                DbUtils.INSTANCE.getDaoSession().getTopicTagDao().insertInTx(topicTagList);

                database.setTransactionSuccessful();
            } catch (Exception e) {
                LOGGER.error("Exception while writing to DB", e);
            } finally {
                DbUtils.endTransaction(database);
            }
        }
    }

    public static void fetchData() {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                e.onNext(downloadJson());
            }

        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final Response response) {
                        Observable.create(new ObservableOnSubscribe<Void>() {
                            @Override
                            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                                processTopicDownload(response);
                                e.onComplete();
                            }

                        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                                .subscribe(new Observer<Void>() {

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        LOGGER.debug("I am done");
                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Void aVoid) {

                                    }
                                });
                    }
                });

    }
}
