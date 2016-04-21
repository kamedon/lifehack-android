package com.kamedon.todo.builder

import com.kamedon.todo.api.TodoClientConfig
import com.kamedon.todo.util.Debug
import com.kamedon.todo.value.okhttp.OkHttp3Error
import com.kamedon.todo.value.okhttp.OkHttp3ErrorEvent
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit

/**
 * Created by kamedon on 2/29/16.
 */
object ApiClientBuilder {

    fun create(todoClientConfig: TodoClientConfig): OkHttpClient {
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
                            403 -> EventBus.getDefault().post(OkHttp3ErrorEvent(OkHttp3Error.InvalidApiKeyOrNotFoundUser, null))
                        }
                        response
                    } catch(e: ConnectException) {
                        EventBus.getDefault().post(OkHttp3ErrorEvent(OkHttp3Error.ConnectException, e))
                        e.printStackTrace()
                        null
                    } catch(e: IOException) {
//                        Debug.d("okhttp", "error:" + e.message)
                        EventBus.getDefault().post(OkHttp3ErrorEvent(OkHttp3Error.Timeout, e))
                        e.printStackTrace()
                        null
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

    }

    //    interface OnRequestListener {
    //        fun onInvalidApiKeyOrNotFoundUser(response: Response);
    //        fun onTimeoutListener(e: IOException);
    //    }
}