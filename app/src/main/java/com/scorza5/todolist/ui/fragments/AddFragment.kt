package com.scorza5.todolist.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.scorza5.todolist.databinding.FragmentAddBinding
import com.scorza5.todolist.core.extensions.format
import com.scorza5.todolist.data.model.Task
import com.scorza5.todolist.presentation.TaskViewModel
import java.util.*

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        insertListeners()

        return binding.root
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
            datePicker.show(parentFragmentManager, "tag")
        }
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener(){
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilHour.editText?.setText("${hour}:${minute}")
            }

            timePicker.show(parentFragmentManager, "tag")
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        binding.btnNewTask.setOnClickListener {
            insertDataToDatabase()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }

    private fun insertDataToDatabase() {
        val title = binding.tilTitle.editText?.text.toString()
        val description = binding.tilDescription.editText?.text.toString()
        val date = binding.tilDate.editText?.text.toString()
        val hour = binding.tilHour.editText?.text.toString()
        val id = 0

        if (inputCheck(title, description, date, hour)){
            val task = Task(id, true,title, description, date, hour)
            mTaskViewModel.addTask(task)
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title: String, description: String, date: String, hour: String): Boolean{
        Log.e("Data:", "2 $title$description$date$hour 2")
        return !(title.isEmpty() && description.isEmpty() && date.isEmpty() && hour.isEmpty())
    }
}