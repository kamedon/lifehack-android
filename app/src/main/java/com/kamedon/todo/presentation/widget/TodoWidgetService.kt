package com.kamedon.todo.presentation.widget

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kamedon.todo.KamedonApplication
import com.kamedon.todo.R
import com.kamedon.todo.domain.entity.Task
import com.kamedon.todo.domain.usecase.task.TaskUserCase
import com.kamedon.todo.presentation.di.ActivityModule
import rx.Observer
import javax.inject.Inject

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return SampleWidgetFactory(packageName, application);
    }


    class SampleWidgetFactory(val packageName: String, val application: Application) : RemoteViewsFactory {

        @Inject
        lateinit var taskUserCase: TaskUserCase

        var tasks: List<Task> = emptyList()

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewAt(position: Int): RemoteViews? {
            Log.d("Widget", "==getViewAt==" + position)
            if (count <= 0) {
                return null;
            }

            var task = tasks[position]
            val rv = RemoteViews(packageName, R.layout.widget_todolist_row)
            rv.setTextViewText(R.id.text, task.body)
            val intent = Intent()
            intent.putExtra("task", task)
            rv.setOnClickFillInIntent(R.id.container, intent)
            return rv
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun onCreate() {
            (application as KamedonApplication).applicationComponent.widgetComponent(ActivityModule()).inject(this);
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun onDataSetChanged() {
            taskUserCase.list(Task.state_untreated, 1)
                    .subscribe(object : Observer<List<Task>> {
                        override fun onError(e: Throwable) {
                        }

                        override fun onNext(list: List<Task>) {
                            tasks = list
                            Log.d("Widget", "onNext" + tasks.size)
                            tasks.forEach {
                                Log.d("Widget", "||" + it.body)
                            }
                        }

                        override fun onCompleted() {

                        }
                    })

        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getCount(): Int {
            Log.d("Widget", "getCount" + tasks.size);
            return tasks.size
        }

        override fun onDestroy() {
        }

    }


}
