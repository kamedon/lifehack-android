package com.kamedon.todo.value.page

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.kamedon.todo.R
import com.kamedon.todo.activity.MainActivity
import com.kamedon.todo.activity.TaskActivity

/**
 * Created by kamedon on 4/22/16.
 */
enum class Page(val nameId: Int, val page: Class<*>) {
    LOGIN(R.string.action_login, MainActivity::class.java),
    TASK_ALL(R.string.title_task_complete, TaskActivity::class.java),
    TASK_UNTREATED(R.string.title_task_complete, TaskActivity::class.java),
    TASK_COMPLETE(R.string.title_task_complete, TaskActivity::class.java),
    LOGOUT(R.string.action_logout, MainActivity::class.java);

    open fun intent(context: Context) = Intent(context, page)
    fun name(resources: Resources) = resources.getString(nameId)
}
