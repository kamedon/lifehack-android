package com.kamedon.todo.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent

/**
 * Created by kamei.hidetoshi on 2016/07/17.
 */
object WidgetUtil {
    fun update(context: Context){
        val widgetUpdate = Intent();
        widgetUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        context.sendBroadcast(widgetUpdate);
    }

}