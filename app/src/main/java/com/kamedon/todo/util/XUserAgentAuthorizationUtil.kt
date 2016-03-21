package com.kamedon.todo.util

import com.kamedon.todo.BuildConfig
import okhttp3.internal.Util

/**
 * Created by kamedon on 2/27/16.
 */
object XUserAgentAuthorizationUtil {
    fun token(): String {
        return token(BuildConfig.X_USER_AGENT_AUTHORIZATION_TOKEN)
    }

    fun token(key: String): String {
        return Util.md5Hex(key);
    }
}


