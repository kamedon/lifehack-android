package com.kamedon.todo

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.kamedon.todo.presentation.di.ApplicationComponent
import com.kamedon.todo.presentation.di.ApplicationModule
import com.kamedon.todo.presentation.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

/**
 * Created by h_kamei on 2016/03/25.
 */
class KamedonApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        Fabric.with(this, Crashlytics());
        LeakCanary.install(this);
    }
}
