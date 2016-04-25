package com.kamedon.todo.domain.entity

import java.io.Serializable

/**
 * Created by kamedon on 2/28/16.
 */
data class User(var id: Int, var username: String, val email: String) : Serializable {
}
