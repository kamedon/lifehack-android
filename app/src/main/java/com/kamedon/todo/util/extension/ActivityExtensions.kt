package com.kamedon.todo.util.extension

import android.app.Activity
import android.content.Intent
import com.kamedon.todo.domain.value.page.Page

inline fun Activity.go(page: Page, f: (intent: Intent) -> Intent) {
    startActivity(f(page.intent(applicationContext)))
}
