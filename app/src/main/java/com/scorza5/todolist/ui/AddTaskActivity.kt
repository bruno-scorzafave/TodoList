package com.scorza5.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.scorza5.todolist.databinding.ActivityAddTaskBinding
import com.scorza5.todolist.extensions.format
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.ui.MainActivity.Companion.CREATE_NEW_TASK
import com.scorza5.todolist.ui.MainActivity.Companion.REQUEST_CODE
import com.scorza5.todolist.viewmodel.TaskViewModel
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        if(intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            var task: Task = mTaskViewModel.findById(taskId)
            binding.tilTitle.editText?.setText(task.title)
            binding.tilDescription.editText?.setText(task.description)
            binding.tilDate.editText?.setText(task.date)
            binding.tilHour.editText?.setText(task.hour)
        }

        insertListeners()
    }

    private fun insertListeners(){
        binding.tilDate.editText?.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.editText?.setText(Date(it + offset).format())

            }
            datePicker.show(supportFragmentManager, "tag")
        }
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener(){
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilHour.editText?.setText("$hour:$minute")
            }

            timePicker.show(supportFragmentManager, "tag")
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnNewTask.setOnClickListener {
            insertDataToDatabase()

            intent = Intent(this, MainActivity::class.java)
            intent.putExtra(REQUEST_CODE, CREATE_NEW_TASK)
            //TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }

    private fun insertDataToDatabase() {
        val title = binding.tilTitle.editText?.text.toString()
        val description = binding.tilDescription.editText?.text.toString()
        val date = binding.tilDate.editText?.text.toString()
        val hour = binding.tilHour.editText?.text.toString()
        val id = intent.getIntExtra(TASK_ID, 0)

        if (inputCheck(title, description, date, hour)){
            val task = Task(id, title, description, date, hour)
            mTaskViewModel.addTask(task)
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title: String, description: String, date: String, hour: String): Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description) && TextUtils.isEmpty(date) && TextUtils.isEmpty(hour))
    }

    companion object{
        const val TASK_ID = "task_id"
    }

}