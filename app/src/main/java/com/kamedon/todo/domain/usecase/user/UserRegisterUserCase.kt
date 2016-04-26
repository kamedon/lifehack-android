package com.kamedon.todo.domain.usecase.user

import com.kamedon.todo.domain.api.TodoApi
import com.kamedon.todo.domain.entity.api.NewUserQuery
import com.kamedon.todo.domain.entity.api.NewUserResponse
import com.kamedon.todo.infra.repository.UserRepository

/**
 * Created by h_kamei on 2016/04/25.
 */
class UserRegisterUserCase(val userApi: TodoApi.UserApi, val userRepository: UserRepository) {
    fun signUp(query: NewUserQuery) = userApi.new(query)

    fun isLogined(): Boolean = userRepository.hasApiKey()

    fun complate(response: NewUserResponse) {
        userRepository.save(response.user)
        userRepository.save(response.api_key)
    }
}
