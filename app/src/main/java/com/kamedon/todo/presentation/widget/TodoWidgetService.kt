package com.kamedon.todo.presentation.widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kamedon.todo.R

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return SampleWidgetFactory(packageName);
    }


    class SampleWidgetFactory(val packageName: String) : RemoteViewsFactory {

        val list: List<String> = listOf("hoge", "foo")

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewAt(position: Int): RemoteViews {
            var text = list[position]
            val rv = RemoteViews(packageName, R.layout.widget_todolist_row)
            rv.setTextViewText(R.id.text, text)
            return rv
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun onCreate() {
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun onDataSetChanged() {
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun onDestroy() {
        }

    }


}
