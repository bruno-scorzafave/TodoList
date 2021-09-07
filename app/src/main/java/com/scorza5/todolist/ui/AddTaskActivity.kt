package com.scorza5.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.scorza5.todolist.databinding.ActivityAddTaskBinding
import com.scorza5.todolist.datasource.TaskDataSource
import com.scorza5.todolist.extensions.format
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.ui.MainActivity.Companion.CREATE_NEW_TASK
import com.scorza5.todolist.ui.MainActivity.Companion.REQUEST_CODE
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findByID(taskId)?.let {
                binding.tilTitle.editText?.setText(it.title)
                binding.tilDescription.editText?.setText(it.description)
                binding.tilDate.editText?.setText(it.date)
                binding.tilHour.editText?.setText(it.hour)
            }
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
            val task = Task(
                title = binding.tilTitle.editText?.text.toString(),
                description = binding.tilDescription.editText?.text.toString(),
                date = binding.tilDate.editText?.text.toString(),
                hour = binding.tilHour.editText?.text.toString(),
                id = intent.getIntExtra(TASK_ID, 0)
            )
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra(REQUEST_CODE, CREATE_NEW_TASK)
            TaskDataSource.insertTask(task)
            Log.e("Task:", TaskDataSource.getList().toString())
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }

}