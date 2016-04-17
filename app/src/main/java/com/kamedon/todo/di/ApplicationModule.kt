package com.kamedon.todo.di

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.kamedon.todo.api.TodoApi
import com.kamedon.todo.api.TodoClientConfig
import com.kamedon.todo.builder.ApiClientBuilder
import com.kamedon.todo.builder.TodoApiBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named
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

    @Provides
    @Singleton
    fun provideHttpClient(todoApiConfig: TodoClientConfig) = ApiClientBuilder.create(todoApiConfig, null)

    @Provides
    @Singleton
    fun provideHttpClientConfig(context: Context) = TodoClientConfig(context);

    @Provides
    @Singleton
    fun provideTodoTaskApi(okHttpClient: OkHttpClient): TodoApi.TaskApi = TodoApiBuilder.buildTaskApi(okHttpClient);

    @Provides
    @Singleton
    fun provideTodoUserApi(okHttpClient: OkHttpClient): TodoApi.UserApi = TodoApiBuilder.buildUserApi(okHttpClient);

}
