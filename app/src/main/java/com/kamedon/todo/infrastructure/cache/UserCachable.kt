package com.kamedon.todo.infrastructure.cache

import com.kamedon.todo.entity.ApiKey
import com.kamedon.todo.entity.User

/**
 * Created by h_kamei on 2016/04/19.
 */
interface UserCachable {
    fun hasApiKey() : Boolean

    fun save(user: User)

    fun save(apiKey: ApiKey)

    fun getApiKey(): ApiKey?

    fun getUser(): User

    fun deleteApiKey(): Any
}
