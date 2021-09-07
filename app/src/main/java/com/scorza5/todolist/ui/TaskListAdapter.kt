package com.scorza5.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.ItemTaskBinding
import com.scorza5.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflator, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class TaskViewHolder(private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task){
            binding.tvTitle.text = item.title
            binding.tvHour.text = item.hour
            binding.ivIcon.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task){
            val ivIcon = binding.ivIcon
            val popupMenu = PopupMenu(ivIcon.context, ivIcon)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_edit -> {listenerEdit(item)}
                    R.id.action_delete -> {listenerDelete(item)}
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id

}