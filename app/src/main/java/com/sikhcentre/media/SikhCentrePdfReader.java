package com.sikhcentre.media;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.network.DownloadFileHandler;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FileUtils;
import com.sikhcentre.utils.UIUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by brinder.singh on 25/03/17.
 */

public enum SikhCentrePdfReader {
    INSTANCE;
    private Disposable disposable;

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileHandler.class);

    public void handlePdfTopic(final Activity activity, Topic topic) {
        if(!FileUtils.isExternalStorageWritable()){
            LOGGER.error("External storage not available, so can't process pdf file");
            UIUtils.showToast(activity, activity.getString(R.string.error_message_no_storage));
            return;
        }

        String url = FileUtils.getTopicPathForDiskStorage(activity, topic);
        if (FileUtils.isTopicDownloadedOnDisk(url)) {
            openPdfFile(activity, url);
        } else {
            Observable<String> observable = DownloadFileHandler.INSTANCE().downloadTopic(activity, topic, url);
            if (disposable != null) {
                disposable.dispose();
            }
            disposable = observable.observeOn(MainSchedulerProvider.INSTANCE.ui()).subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String url) throws Exception {
                    if (url != null) {
                        openPdfFile(activity, url);
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
    }

    private void openPdfFile(Activity activity, String fileUrl) {
        Uri path = Uri.fromFile(new File(fileUrl));
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            activity.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            LOGGER.error("No Application available to view PDF", e);
            UIUtils.showToast(activity, activity.getString(R.string.error_message_pdf_no_app));
        } catch (Exception e) {
            LOGGER.error("Error while opening PDF", e);
            UIUtils.showToast(activity, activity.getString(R.string.error_message_pdf_not_opening));
        }
    }
}
