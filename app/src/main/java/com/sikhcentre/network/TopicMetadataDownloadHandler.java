package com.sikhcentre.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.sikhcentre.R;
import com.sikhcentre.database.DbUtils;
import com.sikhcentre.entities.Author;
import com.sikhcentre.entities.RelatedTopic;
import com.sikhcentre.entities.Tag;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.entities.TopicAuthor;
import com.sikhcentre.entities.TopicTag;
import com.sikhcentre.models.MetaDataResponse;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.NetworkUtils;
import com.sikhcentre.utils.StringUtils;
import com.sikhcentre.utils.UIUtils;

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

    private static MetaDataResponse downloadJson() throws Exception {
        try {
            URL url = new URL("https://dl.dropboxusercontent.com/s/b0puce7rrpxkpjh/response.json");
            String str = StringUtils.toString(url.openStream());
            return new Gson().fromJson(str, MetaDataResponse.class);
        } catch (MalformedURLException e) {
            LOGGER.error("Error while reading url", e);
            throw e;
        } catch (IOException e) {
            LOGGER.error(TAG, "Error while reading file", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(TAG, "Error while populating data from URL", e);
            throw e;
        }
    }

    private static void processTopicDownload(MetaDataResponse metaDataResponse) {
        List<Author> authors = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        List<TopicAuthor> topicAuthorList = new ArrayList<>();
        List<TopicTag> topicTagList = new ArrayList<>();
        List<Topic> topicList = new ArrayList<>();
        List<RelatedTopic> relatedTopicList = new ArrayList<>();

        for (MetaDataResponse.AuthorResponse authorResponse : metaDataResponse.getAuthors()) {
            authors.add(new Author(authorResponse.getId(), authorResponse.getName()));
        }
        for (MetaDataResponse.TagResponse tagResponse : metaDataResponse.getTags()) {
            tags.add(new Tag(tagResponse.getId(), tagResponse.getName()));
        }
        for (MetaDataResponse.TopicResponse topicResponse : metaDataResponse.getTopics()) {
            topicList.add(new Topic(topicResponse.getId(), topicResponse.getTitle(),
                    topicResponse.getUrl(), topicResponse.getInfo(), topicResponse.getContent(),
                    topicResponse.getType()));
            for (Long id : topicResponse.getAuthorIds()) {
                topicAuthorList.add(new TopicAuthor(null, topicResponse.getId(), id));
            }
            for (MetaDataResponse.TopicTagResponse topicTagResponse : topicResponse.getTags()) {
                topicTagList.add(new TopicTag(null, topicResponse.getId(), topicTagResponse.getTagId(), topicTagResponse.getWeight()));
            }
            for (MetaDataResponse.RelatedTopicResponse relatedTopicResponse : topicResponse.getRelatedTopics()) {
                if (!topicResponse.getId().equals(relatedTopicResponse.getTopicId())) {
                    relatedTopicList.add(new RelatedTopic(null, topicResponse.getId(), relatedTopicResponse.getTopicId(), relatedTopicResponse.getWeight()));
                }
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
                DbUtils.INSTANCE.getDaoSession().getRelatedTopicDao().deleteAll();

                DbUtils.INSTANCE.getDaoSession().getAuthorDao().insertInTx(authors);
                DbUtils.INSTANCE.getDaoSession().getTagDao().insertInTx(tags);
                DbUtils.INSTANCE.getDaoSession().getTopicDao().insertInTx(topicList);
                DbUtils.INSTANCE.getDaoSession().getTopicAuthorDao().insertInTx(topicAuthorList);
                DbUtils.INSTANCE.getDaoSession().getTopicTagDao().insertInTx(topicTagList);
                DbUtils.INSTANCE.getDaoSession().getRelatedTopicDao().insertInTx(relatedTopicList);

                database.setTransactionSuccessful();
            } catch (Exception e) {
                LOGGER.error("Exception while writing to DB", e);
            } finally {
                DbUtils.endTransaction(database);
            }
        }
    }

    public static void fetchData(final Context context) {
        if(!NetworkUtils.isNetworkAvailable(context)){
            return;
        }
        final ProgressDialog progressDialog = UIUtils.showProgressBar(context,
                context.getString(R.string.loading_indicator_title),
                context.getString(R.string.loading_indicator_loading_metadata_text));
        Observable.create(new ObservableOnSubscribe<MetaDataResponse>() {
            @Override
            public void subscribe(ObservableEmitter<MetaDataResponse> e) throws Exception {
                e.onNext(downloadJson());
            }

        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.ui())
                .subscribe(new Observer<MetaDataResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        LOGGER.error("Error while downloading metadata", e);
                        handleError(context, progressDialog);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final MetaDataResponse metaDataResponse) {
                        Observable.create(new ObservableOnSubscribe<Void>() {
                            @Override
                            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                                processTopicDownload(metaDataResponse);
                                e.onComplete();
                            }

                        }).observeOn(MainSchedulerProvider.INSTANCE.ui())
                                .subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                                .subscribe(new Observer<Void>() {

                                    @Override
                                    public void onError(Throwable e) {
                                        LOGGER.error("Error while storing metadata", e);
                                        handleError(context, progressDialog);
                                    }

                                    @Override
                                    public void onComplete() {
                                        LOGGER.debug("I am done");
                                        UIUtils.dismissProgressBar(progressDialog);
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

    private static void handleError(Context context, ProgressDialog progressDialog){
        UIUtils.showToast(context, context.getString(R.string.error_message_downloading_metadata));
        UIUtils.dismissProgressBar(progressDialog);
    }
}
