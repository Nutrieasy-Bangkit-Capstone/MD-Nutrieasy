package com.capstone.nutrieasy.ui.authorization

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.databinding.ActivityAuthorizationBinding
import com.capstone.nutrieasy.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    private val viewModel by viewModels<AuthorizationActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(viewModel.user != null){
            viewModel.refreshAccessToken()
        }
        setupState()
    }

    private fun setupState(){
        viewModel.viewState.observe(this){ state ->
            state.apply {
                when{
                    isSuccess -> {
                        startActivity(
                            Intent(baseContext, MainActivity::class.java)
                        )
                        finish()
                    }
                }
            }
        }
    }
}