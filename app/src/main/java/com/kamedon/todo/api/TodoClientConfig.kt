package com.kamedon.todo.api

import com.kamedon.todo.service.UserService
import com.kamedon.todo.util.XUserAgentAuthorizationUtil

/**
 * Created by kamedon on 4/3/16.
 */
class TodoClientConfig(val userService: UserService) {
    fun xAgentKey() = XUserAgentAuthorizationUtil.header()
    fun xAgentToken() = XUserAgentAuthorizationUtil.token()
    fun userToken() = userService.getApiKey();
}
