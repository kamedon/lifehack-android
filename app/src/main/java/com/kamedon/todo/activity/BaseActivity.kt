package com.kamedon.todo.activity

import android.os.Bundle
import com.kamedon.todo.KamedonApplication
import com.kamedon.todo.di.ActivityComponent
import com.kamedon.todo.di.ActivityModule
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

/**
 * Created by kamedon on 4/5/16.
 */
open class BaseActivity : RxAppCompatActivity() {
    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainApplication = application as KamedonApplication;
        activityComponent = mainApplication.applicationComponent.plus(ActivityModule(this));
    }

}
