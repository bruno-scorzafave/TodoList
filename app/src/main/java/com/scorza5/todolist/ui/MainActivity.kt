package com.scorza5.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.scorza5.todolist.R
import com.scorza5.todolist.databinding.ActivityMainBinding
import com.scorza5.todolist.ui.fragments.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /*
    val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result:ActivityResult ->
        val requestCode = result.data?.extras?.getInt(REQUEST_CODE)
        Log.e("Request Code:", requestCode.toString())
        if(requestCode == null){
            Log.e("Result Contract", "Não recebeu nada")
        }
        if(requestCode == CREATE_NEW_TASK){
            Log.e("Result Contract", "Dessa vez recebeu")
            //binding.rvTasks.adapter = adapter
            updateList()
        }
    } */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun onCheck(view: View){
        val fragment = ListFragment()
        fragment.onChecked(view)
    }
}