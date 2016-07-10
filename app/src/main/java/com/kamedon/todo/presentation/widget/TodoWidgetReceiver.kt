package com.kamedon.todo.presentation.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.kamedon.todo.R

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "UPDATE_WIDGET") {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            TodoWidgetProvider.pushWidgetUpdate(context.applicationContext, remoteViews)
        }

    }
}
