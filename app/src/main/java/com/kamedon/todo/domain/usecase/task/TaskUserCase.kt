package com.kamedon.todo.domain.usecase.task

import com.kamedon.todo.domain.api.TodoApi
import com.kamedon.todo.domain.entity.Task
import com.kamedon.todo.domain.entity.api.NewTaskQuery

/**
 * Created by h_kamei on 2016/04/26.
 */
class TaskUserCase(val api: TodoApi.TaskApi) {

    fun edit(task: Task) = api.edit(task.id, task.body, task.state)

    fun delete(task: Task) = api.delete(task.id)

    fun new(query: NewTaskQuery) = api.new(query)

    fun list(state: String, page: Int) = api.list(state, page)

}
