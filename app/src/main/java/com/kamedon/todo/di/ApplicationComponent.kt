package com.kamedon.todo.di

import dagger.Component
import javax.inject.Singleton

/**
 * Created by kamedon on 3/31/16.
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun plus(module: ActivityModule): ActivityComponent;
}
