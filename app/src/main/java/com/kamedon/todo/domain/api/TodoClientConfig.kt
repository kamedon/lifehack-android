package com.kamedon.todo.domain.api

import com.kamedon.todo.BuildConfig
import com.kamedon.todo.infra.repository.UserRepository
import okhttp3.internal.Util

/**
 * Created by kamedon on 4/3/16.
 */
class TodoClientConfig(val userService: UserRepository) {
    fun xAgentKey() = BuildConfig.X_USER_AGENT_AUTHORIZATION_KEY
    fun xAgentToken() =token(BuildConfig.X_USER_AGENT_AUTHORIZATION_TOKEN)
    fun userToken() = userService.getApiKey();

    private fun token(key: String): String {
        return Util.md5Hex(key);
    }
}
