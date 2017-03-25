package com.sikhcentre.utils;

import android.content.Context;
import android.os.Environment;

import com.sikhcentre.entities.Topic;

import java.io.File;

/**
 * Created by brinder.singh on 26/03/17.
 */

public class FileUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getTopicPathForDiskStorage(Context context, Topic topic) {
        return String.format("%s%sTopic%d", getAppDirPathForDiskStorage(context), File.separator, topic.getId());
    }

    public static String getAppDirPathForDiskStorage(Context context){
        return String.format("%s%s%s", context.getExternalFilesDir(null), File.separator, Constants.APP_DIR);
    }

    public static boolean isTopicDownloadedOnDisk(String url) {
        File file = new File(url);
        return file.exists();
    }
}
