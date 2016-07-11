package com.kamedon.todo.presentation.di

import dagger.Component
import javax.inject.Singleton

/**
 * Created by kamedon on 3/31/16.
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun activityComponent(module: ActivityModule): ActivityComponent;
    fun widgetComponent(module: ActivityModule): WidgetComponent;
}
