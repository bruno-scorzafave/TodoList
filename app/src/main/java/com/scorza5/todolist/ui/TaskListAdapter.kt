package com.scorza5.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scorza5.todolist.databinding.ItemTaskBinding
import com.scorza5.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflator, parent, false)
        return  TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class TaskViewHolder(private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task){

        }
    }
}