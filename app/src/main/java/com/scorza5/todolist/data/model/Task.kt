package com.scorza5.todolist.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var active: Boolean,
    val title: String,
    val description: String,
    val date: String,
    val hour: String
):Parcelable { }
