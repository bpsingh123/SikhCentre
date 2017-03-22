package com.sikhcentre.network;

import android.util.Log;

import com.google.gson.Gson;
import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicAuthor;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.models.Response;
import com.sikhcentre.schedulers.MainSchedulerProvider;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by brinder.singh on 21/03/17.
 */

public class TopicDownloadHandler {
    private static final String TAG = "TopicDownloadHandler";

    private static Response downloadJson() {
        try {
            URL url = new URL("https://dl.dropboxusercontent.com/s/b0puce7rrpxkpjh/response.json");
            String str = new Scanner(new BufferedReader(new InputStreamReader(url.openStream()))).useDelimiter("\\Z").next();
            return new Gson().fromJson(str, Response.class);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error while reading url", e);
        } catch (IOException e) {
            Log.e(TAG, "Error while reading file", e);
        } catch (Exception e) {
            Log.e(TAG, "Error while populating data from URL", e);
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
            Log.e(TAG, "Exception while writing to DB", e);
        } finally {
            DbUtils.endTransaction(database);
        }
    }

    public static void fetchData() {
        Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(downloadJson());
            }
        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.computation())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final Response response) {
                        Observable.create(new Observable.OnSubscribe<Void>() {
                            @Override
                            public void call(Subscriber<? super Void> subscriber) {
                                processTopicDownload(response);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                                .subscribe(new Observer<Void>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.i(TAG, "I am done");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Void aVoid) {

                                    }
                                });
                    }
                });

    }
}
