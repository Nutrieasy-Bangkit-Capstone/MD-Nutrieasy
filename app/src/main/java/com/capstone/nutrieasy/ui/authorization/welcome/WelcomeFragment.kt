package com.capstone.nutrieasy.ui.authorization.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentWelcomeBinding
import com.capstone.nutrieasy.ui.authorization.AuthorizationActivity
import com.capstone.nutrieasy.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel: WelcomeFragmentViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                viewModel.signinWithGoogle(account.idToken!!)
            }catch(exc: Exception){
                showToast("Google sign in failed")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        setupView()
        setupAction()
        setupState()
    }

    private fun setupView(){
        if(viewModel.user != null){
            val direction = WelcomeFragmentDirections.actionWelcomeFragmentToBlankFragment()
            findNavController().navigate(direction)
        }
    }

    private fun setupAction(){
        binding.SigninNav.setOnClickListener {
            val directions = WelcomeFragmentDirections.actionWelcomeFragmentToSigninFragment()
            findNavController().navigate(directions)
        }

        binding.SignupNav.setOnClickListener {
            val directions = WelcomeFragmentDirections.actionWelcomeFragmentToSignupFragment()
            findNavController().navigate(directions)
        }

        binding.googleSigninBtn.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){
            if(it.isLoading){
                showLoading()
            }else hideLoading()

            when{
                it.isError -> showToast(it.errorMessage)
                it.isSuccess -> {
                    if(it.user != null){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.googleSigninBtn.text = ""
        (binding.googleSigninBtn as MaterialButton).icon = null
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.googleSigninBtn.text = getString(R.string.google_signin)
        (binding.googleSigninBtn as MaterialButton).icon = ResourcesCompat.getDrawable(
            requireContext().resources, R.drawable.google_icon, null
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}