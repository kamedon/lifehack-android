package com.kamedon.todo.domain.usecase.user

import com.kamedon.todo.domain.api.TodoApi
import com.kamedon.todo.domain.entity.api.LoginUserQuery
import com.kamedon.todo.domain.entity.api.NewUserResponse
import com.kamedon.todo.infra.repository.UserService

/**
 * Created by h_kamei on 2016/04/25.
 */
class LoginUseCase(val userApi: TodoApi.UserApi, val userService: UserService) {
    fun isLogined(): Boolean = userService.hasApiKey()

    fun login(query: LoginUserQuery) = userApi.login(query)
    fun login(response: NewUserResponse) = userService.save(response)


}
