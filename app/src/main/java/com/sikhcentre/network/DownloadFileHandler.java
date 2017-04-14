package com.sikhcentre.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.sikhcentre.R;
import com.sikhcentre.entities.Topic;
import com.sikhcentre.network.clients.DownloadFileClient;
import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.utils.FileUtils;
import com.sikhcentre.utils.NetworkUtils;
import com.sikhcentre.utils.UIUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brinder.singh on 25/03/17.
 */
public class DownloadFileHandler {
    private static DownloadFileHandler downloadFileHandler = new DownloadFileHandler();
    private Retrofit retrofit;
    private Disposable responseBodyObservable;
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileHandler.class);
    private final PublishSubject<String> fileUrlSubject = PublishSubject.create();

    public static DownloadFileHandler INSTANCE() {
        return downloadFileHandler;
    }

    private DownloadFileHandler() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api.url")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Observable<String> downloadTopic(final Context context, final Topic topic, final String filePath) {

        if (!NetworkUtils.isNetworkAvailable(context)) {
            fileUrlSubject.onNext(null);
        }

        final ProgressDialog progressDialog = UIUtils.showProgressBar(context,
                context.getString(R.string.loading_indicator_title),
                context.getString(R.string.loading_indicator_loading_file_text));

        DownloadFileClient downloadFileClient = retrofit.create(DownloadFileClient.class);

        if (responseBodyObservable != null) {
            responseBodyObservable.dispose();
        }

        responseBodyObservable = downloadFileClient.downloadFile(topic.getUrl())
                .subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                .observeOn(MainSchedulerProvider.INSTANCE.ui())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull final ResponseBody responseBody) throws Exception {
                        Observable.create(new ObservableOnSubscribe<Void>() {
                            @Override
                            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                                if (writeResponseBodyToDisk(context, responseBody, filePath)) {
                                    e.onComplete();
                                } else {
                                    e.onError(null);
                                }
                            }

                        }).subscribeOn(MainSchedulerProvider.INSTANCE.computation())
                                .observeOn(MainSchedulerProvider.INSTANCE.ui())
                                .subscribe(new Observer<Void>() {

                                    @Override
                                    public void onError(Throwable e) {
                                        handleError(context, progressDialog);
                                    }

                                    @Override
                                    public void onComplete() {
                                        LOGGER.debug("file download completed", topic);
                                        fileUrlSubject.onNext(filePath);
                                        UIUtils.dismissProgressBar(progressDialog);
                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Void aVoid) {
                                        LOGGER.error("Error occurred while storing file to disk");
                                        handleError(context, progressDialog);
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LOGGER.error("Error occurred downloading file", throwable);
                        handleError(context, progressDialog);
                    }
                });
        return fileUrlSubject;

    }

    private boolean writeResponseBodyToDisk(Context context, ResponseBody body, String url) {
        try {
            File file = new File(FileUtils.getAppDirPathForDiskStorage(context));
            if (!file.exists()) {
                file.mkdirs();
            }

            File futureStudioIconFile = new File(url);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    LOGGER.debug("file download: {} {} {}", fileSizeDownloaded, " of ", fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static void handleError(Context context, ProgressDialog progressDialog) {
        UIUtils.showToast(context, context.getString(R.string.error_message_downloading_metadata));
        UIUtils.dismissProgressBar(progressDialog);
    }

}
