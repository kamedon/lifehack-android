package com.kamedon.todo.presentation.di

import com.kamedon.todo.presentation.activity.MainActivity
import com.kamedon.todo.presentation.activity.TaskActivity
import dagger.Subcomponent

/**
 * Created by kamedon on 4/3/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: TaskActivity)
}

