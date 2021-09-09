package com.scorza5.todolist.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.FragmentListBinding
import com.scorza5.todolist.ui.TaskListAdapter
import com.scorza5.todolist.viewmodel.TaskViewModel

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var mTaskViewModel: TaskViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

        val adapter = TaskListAdapter()
        val recyclerView = binding.rvTasks
        recyclerView.adapter = adapter

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        /*mTaskViewModel.readAllData.observe(viewLifecycleOwner, Observer { task ->
            adapter.setData(task)
        })*/
        mTaskViewModel.readAllData.observeForever{task ->
            adapter.setData(task)
        }
        Log.e("Data recebida:", mTaskViewModel.readAllData.value.toString())

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return binding.root
    }
}