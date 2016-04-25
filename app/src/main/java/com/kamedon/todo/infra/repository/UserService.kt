package com.kamedon.todo.infra.repository

import com.kamedon.todo.domain.entity.api.NewUserResponse
import com.kamedon.todo.infra.repository.UserCachable

/**
 * Created by h_kamei on 2016/03/02.
 */
class UserService(val cache: UserCachable) : UserCachable by  cache {

    fun save(response: NewUserResponse) {
        cache.save(response.user)
        cache.save(response.api_key)
    }
}
