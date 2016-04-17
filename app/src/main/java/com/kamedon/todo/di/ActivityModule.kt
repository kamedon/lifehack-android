package com.kamedon.todo.di

import android.app.Activity
import android.content.Context
import com.kamedon.todo.api.TodoApi
import com.kamedon.todo.api.TodoClientConfig
import com.kamedon.todo.builder.ApiClientBuilder
import com.kamedon.todo.builder.TodoApiBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by kamedon on 4/3/16.
 */
@Module
class ActivityModule(val activity: Activity) {

}
