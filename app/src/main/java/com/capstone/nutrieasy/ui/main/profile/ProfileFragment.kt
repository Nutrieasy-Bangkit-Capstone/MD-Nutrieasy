package com.capstone.nutrieasy.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentProfileBinding
import com.capstone.nutrieasy.ui.authorization.AuthorizationActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<ProfileFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        if(auth.currentUser == null){
            startActivity(Intent(requireActivity(), AuthorizationActivity::class.java))
            requireActivity().finish()
            return
        }

        viewModel.fetchProfile()

        setupState()
        setupView()
        setupAction()
    }

    private fun setupView(){
        binding.apply {
            Picasso.get().load(auth.currentUser?.photoUrl)
                .placeholder(R.drawable.account_circle_dark_24px)
                .error(R.drawable.account_circle_dark_24px)
                .into(binding.imageView)

            displayNameTv.text = auth.currentUser?.displayName
            emailTv.text = auth.currentUser?.email
        }
    }

    private fun setupAction(){
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), AuthorizationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){state ->
            state.apply {
                if(isLoading){
                    showLoading()
                }else hideLoading()

                when{
                    isError -> showToast(errorMessage)
                    isSuccess -> {
                        binding.apply {
                            val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.US)
                            val dateText: String = if(data?.dateOfBirth != null){
                                dateFormat.format(LocalDate.parse(data?.dateOfBirth))
                            }else "-"
                            val weightText: String = if(data?.weight != null){
                                data?.weight.toString()
                            }else "-"
                            val heightText: String = if(data?.height != null){
                                data?.height.toString()
                            }else "-"
                            val genderText: String = if(data?.gender != null){
                                data?.gender!!
                            }else "-"
                            bodTv.text = dateText
                            weightTv.text = weightText
                            heightTv.text = heightText
                            if(data?.gender == null){
                                genderIv.setImageResource(R.drawable.person_24px)
                            }else if(data?.gender?.equals("male", true) == true){
                                genderIv.setImageResource(R.drawable.male_24px)
                            }else{
                                genderIv.setImageResource(R.drawable.female_24px)
                            }
                            genderTv.text = genderText

                            binding.editBtn.setOnClickListener {
                                val directions = ProfileFragmentDirections
                                    .actionProfileFragmentToEditProfileFragment(data!!)
                                findNavController().navigate(directions)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.profileDataLayout.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.profileDataLayout.visibility = View.VISIBLE
    }

    private fun showToast(text: String){
        Snackbar.make(requireContext(), binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}