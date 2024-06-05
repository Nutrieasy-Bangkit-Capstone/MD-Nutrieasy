package com.capstone.nutrieasy.ui.authorization.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentSigninBinding
import com.capstone.nutrieasy.ui.main.MainActivity
import com.capstone.nutrieasy.util.TextChangedListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private val viewModel by viewModels<SigninFragmentViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)!!
            viewModel.signinWithGoogle(account.idToken!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
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

        setupState()
        setupView()
        setupAction()
    }

    private fun setupView(){
        binding.apply {
            emailEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        viewModel.email = text.toString()
                    }
                }
            )
            passwordEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        viewModel.password = text.toString()
                    }
                }
            )
        }
    }

    private fun setupAction(){
        binding.signupNavigation.setOnClickListener {
            val directions = SigninFragmentDirections.actionSigninFragmentToSignupFragment()
            findNavController().navigate(directions)
        }
        binding.signinBtn.setOnClickListener {
            viewModel.signin()
        }
        binding.googleSigninBtn.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){state ->
            state.apply {
                if(isLoading){
                    showLoading()
                }else hideLoading()

                if(isGoogleLoading){
                    showGoogleLoading()
                }else hideGoogleLoading()

                when{
                    isError -> {
                        binding.errorTv.text = errorMessage
                        binding.errorTv.visibility = View.VISIBLE
                    }
                    isSuccess -> {
                        binding.errorTv.text = ""
                        binding.errorTv.visibility = View.INVISIBLE
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
        binding.signinBtn.text = ""
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.signinBtn.text = getString(R.string.sign_in)
    }

    private fun showGoogleLoading(){
        binding.googleProgressBar.visibility = View.VISIBLE
        binding.googleSigninBtn.text = ""
        (binding.googleSigninBtn as MaterialButton).icon = null
    }

    private fun hideGoogleLoading(){
        binding.googleProgressBar.visibility = View.INVISIBLE
        binding.googleSigninBtn.text = getString(R.string.google_signin)
        (binding.googleSigninBtn as MaterialButton).icon = ResourcesCompat.getDrawable(
            requireContext().resources, R.drawable.google_icon, null
        )
    }
}