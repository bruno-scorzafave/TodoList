package com.scorza5.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.scorza5.todolist.datasource.TaskDatabase
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Task>>
    private val taskRepository: TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        readAllData = taskRepository.readAllData
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.addTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.deleteTask(task)
        }
    }

    fun findById(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.findById(id)
        }
    }

}