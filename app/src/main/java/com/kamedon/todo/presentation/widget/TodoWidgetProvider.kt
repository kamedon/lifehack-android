package com.kamedon.todo.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
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

        val TAG = "TodoWidgetProvider"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d(TAG, "=====onUpdate[Widget]====")
        appWidgetIds.forEach {
            val remoteViewsFactoryIntent = Intent(context, TodoWidgetService::class.java);
            val rv = RemoteViews(context.packageName, R.layout.widget_layout);
            rv.setRemoteAdapter(R.id.list, remoteViewsFactoryIntent);
            appWidgetManager.updateAppWidget(it, rv);
        }
    }


}
