package com.capstone.nutrieasy.ui.main.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.capstone.nutrieasy.data.api.model.User
import com.capstone.nutrieasy.databinding.FragmentEditProfileBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val args: EditProfileFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.profileData
        setupView(data)
    }

    private fun setupView(data: User){
        binding.apply {
            val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.US)
            val dateText: String = if(data.dateOfBirth != null){
                dateFormat.format(LocalDate.parse(data.dateOfBirth))
            }else "-"
            val weightText: String = if(data.weight != null){
                data.weight.toString()
            }else "-"
            val heightText: String = if(data.height != null){
                data.height.toString()
            }else "-"
            displayNameEt.setText(data.fullName)
            bodEt.setText(dateText)
            weightEt.setText(weightText)
            heightEt.setText(heightText)
            if(data.gender != null){
                if(data.gender != "Female"){
                    femaleRb.isChecked = false
                    maleRb.isChecked = true
                }else{
                    femaleRb.isChecked = true
                    maleRb.isChecked = false
                }
            }
        }
    }
}