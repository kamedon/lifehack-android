package com.kamedon.todo.api

import android.content.Context
import com.kamedon.todo.infrastructure.cache.UserCachable
import com.kamedon.todo.service.UserService
import com.kamedon.todo.util.XUserAgentAuthorizationUtil

/**
 * Created by kamedon on 4/3/16.
 */
class TodoClientConfig(val userService: UserService) {
    fun xAgentToken() = XUserAgentAuthorizationUtil.token()
    fun userToken() = userService.getApiKey();
}
