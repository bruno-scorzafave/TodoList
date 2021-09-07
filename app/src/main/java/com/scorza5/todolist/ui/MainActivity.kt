package com.scorza5.todolist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorza5.todolist.databinding.ActivityMainBinding
import com.scorza5.todolist.datasource.TaskDataSource
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.ui.AddTaskActivity
import com.scorza5.todolist.ui.AddTaskActivity.Companion.TASK_ID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result:ActivityResult ->
        val requestCode = result.data?.extras?.getInt(REQUEST_CODE)
        Log.e("Request Code:", requestCode.toString())
        if(requestCode == null){
            Log.e("Result Contrat", "Não recebeu nada")
        }
        if(requestCode == CREATE_NEW_TASK){
            Log.e("Result Contrat", "Dessa vez recebeu")
            binding.rvTasks.adapter = adapter
            updateList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners(){
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            resultContract.launch(intent)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(TASK_ID, it.id)
            resultContract.launch(intent)
        }
        adapter.listenerDelete = {
            TaskDataSource.delete(it)
            updateList()
        }
    }

    private fun updateList(){
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
        adapter.submitList(TaskDataSource.getList())
    }

    companion object{
        const val CREATE_NEW_TASK = 1000
        const val REQUEST_CODE = "REQUEST_CODE"
    }
}