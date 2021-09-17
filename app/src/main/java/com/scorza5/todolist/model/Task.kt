package com.scorza5.todolist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var active: Int,
    val title: String,
    val description: String,
    val date: String,
    val hour: String
):Parcelable { }
