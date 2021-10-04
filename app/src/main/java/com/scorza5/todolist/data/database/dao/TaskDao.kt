package com.scorza5.todolist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.scorza5.todolist.data.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY active DESC")
    fun readAllData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun findById(id: Int): Task
}