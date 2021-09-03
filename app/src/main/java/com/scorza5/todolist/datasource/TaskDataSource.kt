package com.scorza5.todolist.datasource

import com.scorza5.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task){
        list.add(task.copy(id = list.size + 1))
    }
}