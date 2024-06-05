package com.capstone.nutrieasy.ui.authorization.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentSignupBinding
import com.capstone.nutrieasy.ui.main.MainActivity
import com.capstone.nutrieasy.util.TextChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<SignupFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setState()
        setupView()
        setupAction()
    }

    private fun setupView(){
        binding.apply {
            nameEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        viewModel.name = text.toString()
                    }
                }
            )

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
        binding.signinNavigation.setOnClickListener {
            val directions = SignupFragmentDirections.actionSignupFragmentToSigninFragment()
            findNavController().navigate(directions)
        }

        binding.signupBtn.setOnClickListener {
            viewModel.signup()
        }
    }

    private fun setState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            state.apply {
                if(isLoading){
                    binding.progressBar.visibility = View.VISIBLE
                    binding.signupBtn.text = ""
                }else{
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.signupBtn.text = getString(R.string.create_account)
                }

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
}