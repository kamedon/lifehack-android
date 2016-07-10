package com.kamedon.todo.presentation.di

import com.kamedon.todo.presentation.widget.TodoWidgetProvider
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
interface WidgetComponet{
    fun inject(widget : TodoWidgetProvider)
}
