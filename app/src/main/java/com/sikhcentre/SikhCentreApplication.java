package com.sikhcentre;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sikhcentre.schedulers.MainSchedulerProvider;
import com.sikhcentre.schedulers.ISchedulerProvider;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class SikhCentreApplication extends Application {
    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return MainSchedulerProvider.INSTANCE;
    }
}
