package com.kamedon.todo.di

import android.app.Application
import android.content.Context
import android.widget.Toast
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by kamedon on 3/31/16.
 */
@Module
class ApplicationModule(val application: Application) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideToast(context: Context) = Toast.makeText(context, "hoge", Toast.LENGTH_SHORT)

}
