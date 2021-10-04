package com.scorza5.todolist.data

import com.scorza5.todolist.data.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    fun insertTask(task: Task){
        if (task.id == 0){
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findByID(taskId: Int) = list.find{ it.id == taskId }

    fun delete(task: Task) {
        list.remove(task)
    }

}