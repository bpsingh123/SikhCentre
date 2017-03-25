package com.sikhcentre.network.clients;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by brinder.singh on 25/03/17.
 */

public interface DownloadFileClient {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
