package com.kamedon.todo.infra.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kamedon.todo.domain.entity.ApiKey
import com.kamedon.todo.domain.entity.User

/**
 * Created by h_kamei on 2016/04/19.
 */
class UserRepositoryImpl(val context: Context) : UserRepository {
    private val key_api_token: String = "key_api_token"
    private val key_user: String = "key_user"

    val pref: SharedPreferences by lazy {
        context.getSharedPreferences("todo_app", Context.MODE_PRIVATE)
    }

    val editor: SharedPreferences.Editor by lazy {
        pref.edit()
    }


    override fun save(user: User) {
        editor.putString(key_user, Gson().toJson(user))
        editor.apply();
    }

    override fun save(apiKey: ApiKey) {
        editor.putString(key_api_token, Gson().toJson(apiKey))
        editor.apply();
    }

    override fun hasApiKey(): Boolean = !pref.getString(key_api_token, "").equals("")

    override fun getApiKey(): ApiKey? = Gson().fromJson(pref.getString(key_api_token, "").toString(), ApiKey::class.java)

    override fun getUser(): User = Gson().fromJson(pref.getString(key_user, "").toString(), User::class.java)

    override fun deleteApiKey() = editor.remove(key_api_token).apply()


}
