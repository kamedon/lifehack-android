package com.kamedon.todo.extension

import android.app.Activity
import android.content.Intent
import com.kamedon.todo.value.page.Page

/**
 * Created by kamedon on 3/3/16.
 */
inline fun <T : Activity> Activity.buildIntent(targetActivity: Class<T>) =
        Intent(applicationContext, targetActivity)

inline fun Activity.go(page: Page, f: (intent: Intent) -> Unit) {
    startActivity(page.intent(applicationContext, f))
}
