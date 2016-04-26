package com.kamedon.todo.domain.usecase.user

import com.kamedon.todo.infra.repository.UserRepository

/**
 * Created by h_kamei on 2016/04/26.
 */
class LogoutUserCase(val userRepository: UserRepository) {
    fun logout() = userRepository.deleteApiKey()
}

