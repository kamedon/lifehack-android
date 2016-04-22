package com.kamedon.todo.value.page

import android.content.Context
import android.content.Intent
import com.kamedon.todo.R
import com.kamedon.todo.activity.MainActivity
import com.kamedon.todo.activity.TaskActivity

/**
 * Created by kamedon on 4/22/16.
 */
enum class Page(val pageName: Int, val c: Class<*>) {
    LOGIN(R.string.action_login, MainActivity::class.java),
    TASK_ALL(R.string.title_task_complete, TaskActivity::class.java),
    TASK_UNTREATED(R.string.title_task_complete, TaskActivity::class.java),
    TASK_COMPLETE(R.string.title_task_complete, TaskActivity::class.java),
    LOGOUT(R.string.action_logout, MainActivity::class.java);

    inline fun intent(context: Context, f: (intent: Intent) -> Unit): Intent {
        val intent = Intent(context, c)
        f(intent)
        return intent
    }

}
