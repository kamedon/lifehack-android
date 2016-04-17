package com.kamedon.todo.api

import android.content.Context
import com.kamedon.todo.service.UserService
import com.kamedon.todo.util.XUserAgentAuthorizationUtil

/**
 * Created by kamedon on 4/3/16.
 */
class TodoClientConfig(val context: Context) {
    fun xAgentToken() = XUserAgentAuthorizationUtil.token()
    fun userToken() = UserService.getApiKey(context);
}
