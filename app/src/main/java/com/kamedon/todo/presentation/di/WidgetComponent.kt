package com.kamedon.todo.presentation.di

import com.kamedon.todo.presentation.widget.TodoWidgetProvider
import com.kamedon.todo.presentation.widget.TodoWidgetService
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface WidgetComponent {
    fun inject(widget: TodoWidgetService.SampleWidgetFactory)
    fun inject(provider: TodoWidgetProvider)
}
