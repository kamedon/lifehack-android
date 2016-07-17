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
import com.kamedon.todo.domain.entity.Task
import com.kamedon.todo.domain.entity.api.NewTaskResponse
import com.kamedon.todo.domain.usecase.task.TaskUserCase
import com.kamedon.todo.presentation.activity.TaskEditDialogActivity
import com.kamedon.todo.presentation.di.ActivityModule
import com.kamedon.todo.presentation.di.ApplicationComponent
import com.kamedon.todo.presentation.di.ApplicationModule
import com.kamedon.todo.presentation.di.DaggerApplicationComponent
import com.kamedon.todo.util.Debug
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by kamei.hidetoshi on 2016/07/09.
 */
class TodoWidgetProvider : AppWidgetProvider() {
    private var component: ApplicationComponent? = null

    @Inject
    lateinit var taskUserCase: TaskUserCase

    companion object {
        val TAG = "TodoWidgetProvider"
        val ACTION_ITEM_CLICK = "com.kamedon.todo..ACTION_ITEM_CLICK"
        val ACTION_CLICK = "com.kamedon.todo.ACTION_TASK_REGISTER"
    }


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d(TAG, "=====onUpdate[Widget]====")
        appWidgetIds.forEach {
            Log.d(TAG, "[$it]")
            val remoteViewsFactoryIntent = Intent(context, TodoWidgetService::class.java);
            val rv = RemoteViews(context.packageName, R.layout.widget_layout)
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

            val intent = Intent(ACTION_CLICK)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, it);
            val itemRegisterPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            rv.setOnClickPendingIntent(R.id.btn_register, itemRegisterPendingIntent);

            appWidgetManager.updateAppWidget(it, rv);
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Debug.d("response", "[onReceive start]")
        inject(context)
        when (intent.action) {
            ACTION_CLICK -> {
                val intent = Intent(context, TaskEditDialogActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                try {
                    pendingIntent.send()
                } catch (e: PendingIntent.CanceledException) {
                    e.printStackTrace()
                }
            }
            ACTION_ITEM_CLICK -> {
                val task = intent.getSerializableExtra("task") as Task
                task.state = Task.state_complete
                Debug.d("response", "== Task Edit==");
                taskUserCase.edit(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<NewTaskResponse> {
                            override fun onNext(response: NewTaskResponse) {
                                Debug.d("response", response.toString())
                            }

                            override fun onCompleted() {
                                updateView(context)
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
            }
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                updateView(context)
            }
            else -> {
                Debug.d("response", "== ELSE==" + intent.action);
            }
        }

    }

    private fun inject(context: Context) {
        if (component == null) {
            component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(context)).build()
            component?.widgetComponent(ActivityModule())?.inject(this)
        }
    }

    fun updateView(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val myWidget = ComponentName(context, TodoWidgetProvider::class.java)
        val appWidgetIds = manager.getAppWidgetIds(myWidget)
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)
    }

}
