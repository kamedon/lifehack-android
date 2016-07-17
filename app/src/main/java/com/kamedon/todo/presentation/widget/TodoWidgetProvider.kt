package com.kamedon.todo.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.kamedon.todo.R

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetProvider : AppWidgetProvider() {


    companion object {
        val TAG = "TodoWidgetProvider"
        val ACTION_ITEM_CLICK = "com.kamedon.todo..ACTION_ITEM_CLICK"
        val ACTION_CLICK = "com.kamedon.todo.ACTION_CLICK"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d(TAG, "=====onUpdate[Widget]====")
        appWidgetIds.forEach {
            val remoteViewsFactoryIntent = Intent(context, TodoWidgetService::class.java);
            val rv = RemoteViews(context.packageName, R.layout.widget_layout);
            rv.setRemoteAdapter(R.id.list, remoteViewsFactoryIntent);
            val itemClickIntent = Intent(context, TodoWidgetProvider::class.java);
            itemClickIntent.action = ACTION_ITEM_CLICK;
            val itemClickPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    itemClickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            rv.setPendingIntentTemplate(R.id.list, itemClickPendingIntent);

            appWidgetManager.updateAppWidget(it, rv);
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_CLICK -> {
            }
            ACTION_ITEM_CLICK -> {
                Toast.makeText(context, "ACTION_ITEM_CLICK", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }

    }


}
