package com.kamedon.todo.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.kamedon.todo.R

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetProvider : AppWidgetProvider() {
    companion object {
        fun pushWidgetUpdate(context: Context, remoteViews: RemoteViews) {
            var myWidget = ComponentName(context, TodoWidgetProvider::class.java);
            var manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(myWidget, remoteViews);
        }

        fun clickButton(context: Context): PendingIntent {
            var intent = Intent();
            intent.action = "UPDATE_WIDGET";
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        var remoteViews = RemoteViews(context.packageName, R.layout.widget_layout);

        // ボタンイベントを登録
//        remoteViews.setOnClickPendingIntent(R.id.button, clickButton(context));

        // テキストフィールドに"初期画面"と表示
        remoteViews.setTextViewText(R.id.title, "初期画面");

        // アップデートメソッド呼び出し
        pushWidgetUpdate(context, remoteViews);
    }

    // アップデート

}
