package com.sikhcentre.media;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.sikhcentre.entities.Topic;
import com.sikhcentre.network.DownloadFileHandler;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FileUtils;

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
//        if(!FileUtils.isExternalStorageWritable()){
//            LOGGER.error("External storage not available, so can't process pdf file");
//
//            //TODO show some error message
//            return;
//        }

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
                    openPdfFile(activity, url);
                }
            });
        }
    }

    private void openPdfFile(Activity activity, String fileUrl) {
        Uri path = Uri.fromFile(new File(fileUrl));
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try{
            activity.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            LOGGER.error("No Application available to view PDF", e);
            Toast.makeText(activity, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            LOGGER.error("Error while opening PDF", e);
            Toast.makeText(activity, "Error while opening PDFs", Toast.LENGTH_SHORT).show();
        }
    }
}
