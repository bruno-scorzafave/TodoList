package com.scorza5.todolist.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.scorza5.todolist.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY active")
    fun readAllData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun findById(id: Int): Task
}