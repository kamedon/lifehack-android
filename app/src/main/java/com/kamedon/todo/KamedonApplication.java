package com.kamedon.todo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by h_kamei on 2016/03/25.
 */
public class KamedonApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        LeakCanary.install(this);
    }
}
