package com.sikhcentre.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by brinder.singh on 25/02/17.
 */

public interface ISchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler ui();
}
