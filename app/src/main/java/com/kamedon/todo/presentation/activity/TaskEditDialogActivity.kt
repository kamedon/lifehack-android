package com.kamedon.todo.presentation.activity

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.kamedon.todo.R
import com.kamedon.todo.domain.entity.Task
import com.kamedon.todo.domain.entity.api.NewTaskQuery
import com.kamedon.todo.domain.entity.api.NewTaskResponse
import com.kamedon.todo.domain.usecase.task.TaskUserCase
import com.kamedon.todo.util.Debug
import com.kamedon.todo.util.WidgetUtil
import com.kamedon.todo.util.extension.observable
import rx.Observer
import javax.inject.Inject

class TaskEditDialogActivity : BaseActivity(), TaskEditView {

    @Inject
    lateinit var taskUseCase: TaskUserCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_task_edit_dialog)
        val fragment = AlertDialogFragment()
        fragment.show(supportFragmentManager, "alert_dialog")
    }

    override fun inputBody(body: String) {
        observable(taskUseCase.new(NewTaskQuery(body, Task.state_untreated)))
                .subscribe(object : Observer<NewTaskResponse> {
                    override fun onNext(t: NewTaskResponse) {
                        Debug.d("response", t.toString())
                    }

                    override fun onCompleted() {
                        WidgetUtil.update(applicationContext)
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        finish()
                    }
                })
    }

}

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.dialog_widget_edit_task, null)
        val dialog = AlertDialog.Builder(activity).setView(view).setPositiveButton(android.R.string.ok) {
            d, which ->
            if (activity is TaskEditView) {
                val text = (view.findViewById(R.id.edit_body) as EditText).text.toString()
                (activity as TaskEditView).inputBody(text)
            }
        }.create()
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

}

interface TaskEditView {
    fun inputBody(body: String)
}

