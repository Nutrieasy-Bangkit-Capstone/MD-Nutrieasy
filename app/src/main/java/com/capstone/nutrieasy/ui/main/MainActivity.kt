package com.capstone.nutrieasy.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.navView.setOnItemSelectedListener {item ->
            val navController = binding.navHostFragment
                .getFragment<Fragment>().findNavController()
            when(item.itemId){
                R.id.navigation_home -> {
                    navController.navigate(R.id.action_global_navigation_home)
                    true
                }
                R.id.navigation_scan -> {
                    navController.navigate(R.id.action_global_scanFragment)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.action_global_profileFragment)
                    true
                }
                else -> true
            }
        }
    }
}