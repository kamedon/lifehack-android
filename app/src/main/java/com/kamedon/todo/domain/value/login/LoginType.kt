package com.kamedon.todo.domain.value.login

/**
 * Created by h_kamei on 2016/04/21.
 */
enum class LoginType {
    NEW, LOGIN, ALREADY ;

    companion object {
        fun key() = "login"
    }
}
