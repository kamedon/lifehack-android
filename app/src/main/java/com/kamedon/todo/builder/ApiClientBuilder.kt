package com.kamedon.todo.builder

import com.kamedon.todo.api.TodoClientConfig
import com.kamedon.todo.util.Debug
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by kamedon on 2/29/16.
 */
object ApiClientBuilder {

    fun create(todoClientConfig: TodoClientConfig, listener: OnRequestListener? = null): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor({
                    chain ->
                    val original = chain.request()
                    val builder = original.newBuilder()
                            .header(todoClientConfig.xAgentKey(), todoClientConfig.xAgentToken())
                            .header("Accept", "application/json")
                            //.header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());

                    Debug.d("okhttp", "token:${todoClientConfig.xAgentToken()}")

                    val apiKey = todoClientConfig.userToken();
                    Debug.d("okhttp", "user:${apiKey?.token}")
                    apiKey?.let {
                        if (!it.token.equals("")) {
                            builder.header("Authorization", it.token)
                        }

                    }
                    Debug.d("okhttp", "token:set")

                    try {
                        var response = chain.proceed(builder.build())
                        Debug.d("okhttp", "response:${response?.toString()}")
                        when (response?.code()) {
                            403 -> listener?.onInvalidApiKeyOrNotFoundUser(response)
                        }
                        response
                    } catch(e: IOException) {
                        Debug.d("okhttp", "error:" + e.message)
                        listener?.onTimeoutListener(e)
                        e.printStackTrace()
                        null
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

    }

    interface OnRequestListener {
        fun onInvalidApiKeyOrNotFoundUser(response: Response);
        fun onTimeoutListener(e: IOException);
    }
}