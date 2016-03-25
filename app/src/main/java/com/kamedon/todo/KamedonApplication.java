package com.kamedon.todo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by h_kamei on 2016/03/25.
 */
public class KamedonApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
