package com.sikhcentre.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by brinder.singh on 25/03/17.
 */
public class MailUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailUtils.class);

    public static void sendMail(Activity activity, String[] mailIDs, String subject, String text, ArrayList<Uri> uri) {
        // create intent to send mail
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailIDs);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (uri != null) {
            LOGGER.debug("Mail with attachments");
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri);
        }

        try {
            activity.startActivity(createEmailOnlyChooserIntent(activity, emailIntent, "Send via email"));
            LOGGER.debug("Sent Mail");
        } catch (ActivityNotFoundException ex) {
            LOGGER.error("No email clients installed.", ex);
        }
    }

    /*
     * To filter only email clients
     */
    private static Intent createEmailOnlyChooserIntent(Activity activity, Intent source,
                                                       CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = activity.getPackageManager()
                .queryIntentActivities(i, 0);

        for (ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if (!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }

}
