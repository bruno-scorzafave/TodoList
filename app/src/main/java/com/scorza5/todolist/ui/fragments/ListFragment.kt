package com.scorza5.todolist.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.FragmentListBinding
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.ui.DiffCallback
import com.scorza5.todolist.ui.TaskListAdapter
import com.scorza5.todolist.viewmodel.TaskViewModel

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var mTaskViewModel: TaskViewModel
    private val adapter by lazy { TaskListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)

        val recyclerView = binding.rvTasks
        recyclerView.adapter = adapter

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        updateList()

        adapter.listenerEdit = {
            val bundle = Bundle(1)
            bundle.putParcelable("task", it)
            findNavController().navigate(R.id.action_listFragment_to_updateFragment, bundle)
        }
        adapter.listenerDelete = {
            mTaskViewModel.deleteTask(it)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return binding.root
    }

    private fun updateList(){

        mTaskViewModel.readAllData.observe(viewLifecycleOwner, { task ->
            adapter.submitList(task)
        })

    }
}