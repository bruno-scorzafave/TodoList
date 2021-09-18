package com.scorza5.todolist.ui

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.ItemTaskBinding
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.repository.TaskRepository
import java.util.*

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    private var taskList = emptyList<Task>()
    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}
    var listenerCheckBox: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        //holder.bind(getItem(position))
        val currentItem = currentList[position]
        holder.binding.tvTitle.text = currentItem.title
        holder.binding.tvDesc.text = currentItem.description
        holder.binding.tvHour.text = "${currentItem.date} ${currentItem.hour}"
        holder.binding.ivIcon.setOnClickListener{
            holder.showPopup(currentItem)
        }

        holder.binding.checkBox.setOnClickListener {
            currentItem.active = holder.binding.checkBox.isChecked
            Log.v("active = ", "currentItem.active")
        }
        listenerCheckBox(currentItem)

        isActive(currentItem.active, holder)

    }

    fun getList(): List<Task>{
        return this.taskList
    }
    private fun isActive(active: Boolean, holder: TaskViewHolder){
        if (active){
            holder.binding.checkBox.isChecked = false
            holder.binding.tvTitle.paintFlags = holder.binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvHour.paintFlags = holder.binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvDesc.paintFlags = holder.binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else{
            holder.binding.checkBox.isChecked = true
            holder.binding.tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvDesc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvHour.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){

        fun showPopup(item: Task){
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