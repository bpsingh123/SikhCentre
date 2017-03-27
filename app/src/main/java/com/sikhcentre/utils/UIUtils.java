package com.sikhcentre.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by brinder.singh on 27/03/17.
 */

public class UIUtils {
    public static ProgressDialog showProgressBar(Context context, String title, String message) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
        return progress;
    }

    public static void dismissProgressBar(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showToast(Context context, String text, int duration){
        Toast.makeText(context, text, duration).show();
    }
}
