package com.capstone.nutrieasy.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentProfileBinding
import com.capstone.nutrieasy.ui.authorization.AuthorizationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

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
        setupView()
        setupAction()
    }

    private fun setupView(){
        binding.apply {
            Picasso.get().load(auth.currentUser?.photoUrl)
                .placeholder(R.drawable.account_circle_24px)
                .error(R.drawable.account_circle_24px)
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
}