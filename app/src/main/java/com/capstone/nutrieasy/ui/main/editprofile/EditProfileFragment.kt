package com.capstone.nutrieasy.ui.main.editprofile

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.User
import com.capstone.nutrieasy.databinding.FragmentEditProfileBinding
import com.capstone.nutrieasy.util.TextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val args: EditProfileFragmentArgs by navArgs()
    private val viewModel by viewModels<EditProfileFragmentViewModel>()
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

        setupAction()
        setupView(data)
        setupState()
    }

    private fun setupView(data: User){
        binding.apply {
            val dateText: String = data.dateOfBirth ?: ""
            val weightText: String = if(data.weight != null){
                data.weight.toString()
            }else ""
            val heightText: String = if(data.height != null){
                data.height.toString()
            }else ""
            val activityList = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
            val activityDropdown = (activityEt as MaterialAutoCompleteTextView)
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, activityList)
            activityDropdown.setAdapter(adapter)
            activityDropdown.keyListener = null

            displayNameEt.setText(data.fullName)
            bodEt.setText(dateText)
            weightEt.setText(weightText)
            heightEt.setText(heightText)
            if(!data.activityLevel.isNullOrEmpty()){
                val index = activityList.indexOf(data.activityLevel)
                viewModel.activity = data.activityLevel
                Log.d("index dropdown", index.toString())
                activityDropdown.setText(adapter.getItem(index), false)
            }
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

    private fun setupAction(){
        binding.apply {
            displayNameEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        viewModel.displayName = text.toString()
                    }
                }
            )

            bodEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        viewModel.bod = text.toString()
                    }
                }
            )

            bodTextField.setEndIconOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.addOnPositiveButtonClickListener {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val dateText: String = dateFormat.format(Date(it))
                    bodEt.setText(dateText)
                }

                datePicker.show(parentFragmentManager, "tag")
            }

            heightEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        viewModel.height = text.toString().toIntOrNull() ?: 0
                    }
                }
            )

            weightEt.addTextChangedListener(
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        viewModel.weight = text.toString().toIntOrNull() ?: 0
                    }
                }
            )

            val activityDropdown = (activityEt as MaterialAutoCompleteTextView)
            activityDropdown.setOnItemClickListener { _, _, _, _ ->
                viewModel.activity = activityDropdown.text.toString()
            }

            activityDropdown.addTextChangedListener {
                object: TextChangedListener(){
                    override fun onTextChanged(
                        text: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        viewModel.activity = text.toString()
                    }
                }
            }

            maleRb.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    viewModel.gender = "Male"
                    femaleRb.isChecked = false
                }
            }
            femaleRb.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    viewModel.gender = "Female"
                    maleRb.isChecked = false
                }
            }

            saveBtn.setOnClickListener {
                viewModel.updateProfile()
            }
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            state.apply {
                if(isLoading){
                    showLoading()
                }else hideLoading()

                when{
                    isError -> {
                        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
                            .setAction("Retry") {
                                viewModel.updateProfile()
                            }
                            .show()
                    }
                    isSuccess -> {
                        val dialog = MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Profile Updated")
                            .setMessage("Yayy!!, Your profile has been updated. Now you can get more insight about your daily nutrition based on your profile data")
                            .setIcon(R.drawable.save_24px)
                            .setPositiveButton("Ok") { dialog, _ ->
                                findNavController().navigateUp()
                                dialog.dismiss()
                            }
                        dialog.show()
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.saveBtn.text = ""
        (binding.saveBtn as MaterialButton).icon = null
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.saveBtn.text = getString(R.string.save_profile)
        (binding.saveBtn as MaterialButton).setIconResource(R.drawable.save_24px)
    }
}