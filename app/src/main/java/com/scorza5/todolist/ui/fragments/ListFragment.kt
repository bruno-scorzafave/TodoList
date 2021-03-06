package com.scorza5.todolist.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.FragmentListBinding
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
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        //mTaskViewModel.init()
        recyclerView.adapter = adapter

        updateList()
        emptyState(true)

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
            if (task.isNotEmpty()){
                emptyState(false)
            } else{
                emptyState(true)
            }
            adapter.submitList(task)
        })

    }

    private fun emptyState(visibility: Boolean){
        binding.includeEmpty.root.visibility = if (visibility) View.VISIBLE else View.GONE
    }
}