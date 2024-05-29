package com.capstone.nutrieasy.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.navView.setOnItemSelectedListener {item ->
            val navController = binding.navHostFragment
                .getFragment<Fragment>().findNavController()
            when(item.itemId){
                R.id.navigation_home -> {
                    navController.navigate(R.id.action_global_navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.action_global_navigation_dashboard)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.action_global_navigation_notifications)
                    true
                }
                else -> true
            }
        }
    }
}