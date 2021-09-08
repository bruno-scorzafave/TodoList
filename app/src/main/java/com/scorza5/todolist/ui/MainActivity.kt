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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorza5.todolist.databinding.ActivityMainBinding
import com.scorza5.todolist.model.Task
import com.scorza5.todolist.ui.AddTaskActivity
import com.scorza5.todolist.ui.AddTaskActivity.Companion.TASK_ID
import com.scorza5.todolist.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
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
            //binding.rvTasks.adapter = adapter
            updateList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        updateList()



        insertListeners()
    }

    private fun insertListeners(){
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            //resultContract.launch(intent)
            startActivity(intent)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(TASK_ID, it.id)
            resultContract.launch(intent)
        }
        adapter.listenerDelete = {
            //TaskDataSource.delete(it) updateList()
            mTaskViewModel.deleteTask(it)
        }
    }

    private fun updateList(){

        val list = mTaskViewModel.readAllData.toString()
        Log.e("Data retornada", list)
        binding.includeEmpty.emptyState.visibility = if(list.isNullOrEmpty() ) View.VISIBLE else View.GONE
        mTaskViewModel.readAllData.observe(this, Observer { task ->
            adapter.setData(task)
        })
    }

    companion object{
        const val CREATE_NEW_TASK = 1000
        const val REQUEST_CODE = "REQUEST_CODE"
    }
}