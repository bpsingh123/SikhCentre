package com.sikhcentre.media;

import android.app.Activity;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.network.DownloadFileHandler;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FileUtils;
import com.sikhcentre.utils.UIUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.sikhcentre.media.PdfUtils.openPdfFile;

/**
 * Created by brinder.singh on 01/04/17.
 */

public class TopicMediaHandler {
    private Disposable disposable;
    private Topic topic;
    private Activity activity;
    private String filePathOnDisk;
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileHandler.class);

    public TopicMediaHandler(Topic topic, Activity activity) {
        this.topic = topic;
        this.activity = activity;
        filePathOnDisk = FileUtils.getTopicPathForDiskStorage(activity, topic);
    }

    public boolean isTopicDownloaded() {
        return FileUtils.isTopicDownloadedOnDisk(filePathOnDisk);
    }

    public void openTopic() {
        switch (topic.getTopicType()) {
            case PDF:
                openPdfFile(activity, filePathOnDisk);
                break;
        }
    }

    public void downloadTopic() {
        Observable<String> observable = DownloadFileHandler.INSTANCE().downloadTopic(activity, topic, filePathOnDisk);
        unBind();
        disposable = observable.observeOn(MainSchedulerProvider.INSTANCE.ui()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String url) throws Exception {
                if (url != null) {
                    openTopic();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.error("Error while downloading topic", throwable);
                UIUtils.showToast(activity, activity.getString(R.string.error_message_downloading_file));
            }
        });
    }

    public void deleteTopic(){
        FileUtils.deleteFile(filePathOnDisk);
    }

    public void unBind(){
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
