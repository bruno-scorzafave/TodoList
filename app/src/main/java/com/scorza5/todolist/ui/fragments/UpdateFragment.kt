package com.scorza5.todolist.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.FragmentUpdateBinding
import com.scorza5.todolist.extensions.format
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.viewmodel.TaskViewModel
import java.util.*

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var task: Task
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(layoutInflater)
        task = requireArguments().getParcelable<Task>("task")!!

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.tilUpdateTitle.editText!!.setText(task.title)
        binding.tilUpdateDescription.editText!!.setText(task.description)
        binding.tilUpdateDate.editText!!.setText(task.date)
        binding.tilUpdateHour.editText!!.setText(task.hour)

        insertListeners()

        return binding.root
    }

    private fun insertListeners(){
        binding.tilUpdateDate.editText?.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilUpdateDate.editText?.setText(Date(it + offset).format())

            }
            datePicker.show(parentFragmentManager, "tag")
        }
        binding.tilUpdateHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener(){
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilUpdateHour.editText?.setText("${hour}:${minute}")
            }

            timePicker.show(parentFragmentManager, "tag")
        }
        binding.btnUpdateCancel.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        binding.btnUpdateTask.setOnClickListener {
            insertDataToDatabase()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun insertDataToDatabase() {
        val title = binding.tilUpdateTitle.editText?.text.toString()
        val description = binding.tilUpdateDescription.editText?.text.toString()
        val date = binding.tilUpdateDate.editText?.text.toString()
        val hour = binding.tilUpdateHour.editText?.text.toString()
        val id = task.id

        if (inputCheck(title, description, date, hour)){
            val task = Task(id, true, title, description, date, hour)
            mTaskViewModel.updateTask(task)
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title: String, description: String, date: String, hour: String): Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description) && TextUtils.isEmpty(date) && TextUtils.isEmpty(hour))
    }

}