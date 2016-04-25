package com.kamedon.todo.domain.value.event.okhttp

/**
 * Created by h_kamei on 2016/04/21.
 */
data class OkHttp3ErrorEvent(val error: OkHttp3Error, val exception: Exception?)

enum class OkHttp3Error {
    ConnectException {
        override fun msg() = "network error"
    },
    Timeout {
        override fun msg() = "timeout error"
    },
    InvalidApiKeyOrNotFoundUser {
        override fun msg() = "InvalidApiKeyOrNotFoundUser"
    };

    abstract fun msg(): String;


}
