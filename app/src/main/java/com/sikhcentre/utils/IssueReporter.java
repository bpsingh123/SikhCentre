package com.sikhcentre.utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by brinder.singh on 28/03/17.
 */

public class IssueReporter {
    private static final String[] emailIds = {"brinderpal.singh@gmail.com"};
    private static final String reportSubject = "SikhCentre App Issue";
    public static void report(Context context){
        MailUtils.sendMail((Activity) context, emailIds,
                reportSubject, "", null);
    }
}
