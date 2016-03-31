package com.kamedon.todo.di

import com.kamedon.todo.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by kamedon on 3/31/16.
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}
