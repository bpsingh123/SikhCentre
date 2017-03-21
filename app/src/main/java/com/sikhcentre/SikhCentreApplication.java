package com.sikhcentre;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sikhcentre.database.DbUtils;
import com.sikhcentre.schedulers.ISchedulerProvider;
import com.sikhcentre.schedulers.MainSchedulerProvider;

/**
 * Created by brinder.singh on 25/02/17.
 */

public class SikhCentreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return MainSchedulerProvider.INSTANCE;
    }

    public void initialize(){
        DbUtils.INSTANCE.init(getApplicationContext());
    }
}
