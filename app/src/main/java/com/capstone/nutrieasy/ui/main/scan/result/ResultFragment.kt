package com.capstone.nutrieasy.ui.main.scan.result

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentResultBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val navArgs: ResultFragmentArgs by navArgs()
    private val viewModel by viewModels<ResultFragmentViewModel>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        viewModel.scan(auth.uid!!, navArgs.imageUri.toUri(), requireContext())
        setupState()
        setupView()
    }

    private fun setupView(){
        binding.foodIv.setImageURI(
            navArgs.imageUri.toUri()
        )
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            state.apply {
                if(isLoading){
                    showLoading()
                }else hideLoading()

                when{
                    isError -> {
                        val dialog = MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage(errorMessage)
                            .setIcon(R.drawable.error_24px)
                            .setPositiveButton("Ok") { dialog, _ ->
                                findNavController().popBackStack()
                                dialog.dismiss()
                            }
                        dialog.show()
                    }
                    isSuccess -> {
                        binding.nameTv.text = data?.foodName
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTv.visibility = View.VISIBLE
        binding.nameTv.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.loadingTv.visibility = View.INVISIBLE
        binding.nameTv.visibility = View.VISIBLE
    }
}