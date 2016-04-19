package com.kamedon.todo.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kamedon.todo.entity.ApiKey
import com.kamedon.todo.entity.User
import com.kamedon.todo.entity.api.NewUserResponse
import com.kamedon.todo.infrastructure.cache.UserCacheable
import javax.inject.Inject

/**
 * Created by h_kamei on 2016/03/02.
 */
class UserService(val cache: UserCacheable) : UserCacheable by  cache {

    fun update(response: NewUserResponse) {
        cache.putLoginUser(response.user)
        cache.putApiKey(response.api_key)
    }
}
