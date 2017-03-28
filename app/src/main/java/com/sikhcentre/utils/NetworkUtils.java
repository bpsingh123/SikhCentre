package com.sikhcentre.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sikhcentre.R;

/**
 * Created by brinder.singh on 28/03/17.
 */

public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(!isAvailable) {
            UIUtils.showToast(context, context.getString(R.string.error_message_no_internet));
        }
        return isAvailable;
    }
}
