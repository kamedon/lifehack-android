package com.kamedon.todo.util

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.kamedon.todo.BuildConfig
import com.kamedon.todo.domain.entity.User

object Debug {
    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    fun d(tag: String, msg: String, e: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, e);
        }
    }


}

fun String.logd(tag: String) {
    Debug.d(tag, this)
}

fun String.logd(tag: String, e: Throwable) {
    Debug.d(tag, this, e)
}

inline fun User.setupCrashlytics() {
    Crashlytics.setUserIdentifier(id.toString());
    Crashlytics.setUserEmail(email);
    Crashlytics.setUserName(username);
}
