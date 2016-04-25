package com.kamedon.todo.infra.repository

import com.kamedon.todo.domain.entity.ApiKey
import com.kamedon.todo.domain.entity.User

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
