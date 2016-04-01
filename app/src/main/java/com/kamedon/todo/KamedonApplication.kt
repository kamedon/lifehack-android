package com.kamedon.todo

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.kamedon.todo.di.ApplicationComponent
import com.kamedon.todo.di.ApplicationModule
import com.kamedon.todo.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

/**
 * Created by h_kamei on 2016/03/25.
 */
class KamedonApplication : Application() {
    lateinit var applicationModule: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationModule = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        Fabric.with(this, Crashlytics());
        LeakCanary.install(this);
    }
}
