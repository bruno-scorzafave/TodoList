package com.scorza5.todolist.repository

import androidx.lifecycle.LiveData
import com.scorza5.todolist.datasource.TaskDao
import com.scorza5.todolist.model.Task

class TaskRepository(private val taskDao: TaskDao) {
    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task){
        taskDao.addTask(task)
    }
    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }
    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }
    fun findById(id: Int): Task = taskDao.findById(id)
}
