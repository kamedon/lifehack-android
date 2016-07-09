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

            val i = Math.random()
            // テキストをクリック回数を元に更新
            remoteViews.setTextViewText(R.id.title, "クリック回数: " + i)

            // もう一回クリックイベントを登録(毎回登録しないと上手く動かず)
            remoteViews.setOnClickPendingIntent(R.id.button, TodoWidgetProvider.clickButton(context))

            TodoWidgetProvider.pushWidgetUpdate(context.applicationContext, remoteViews)
        }

    }
}
