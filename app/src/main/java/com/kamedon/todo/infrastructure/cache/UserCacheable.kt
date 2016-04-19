package com.kamedon.todo.infrastructure.cache

import com.kamedon.todo.entity.ApiKey
import com.kamedon.todo.entity.User

/**
 * Created by h_kamei on 2016/04/19.
 */
interface UserCacheable {
    fun hasApiKey() : Boolean

    fun putLoginUser(user: User)

    fun putApiKey(apiKey: ApiKey)

    fun getApiKey(): ApiKey?

    fun getUser(): User

    fun deleteApiKey(): Any
}
