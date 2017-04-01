package com.sikhcentre.media;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.sikhcentre.R;
import com.sikhcentre.network.DownloadFileHandler;
import com.sikhcentre.utils.UIUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by brinder.singh on 01/04/17.
 */

public class PdfUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileHandler.class);
    public static void openPdfFile(Activity activity, String fileUrl) {
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
