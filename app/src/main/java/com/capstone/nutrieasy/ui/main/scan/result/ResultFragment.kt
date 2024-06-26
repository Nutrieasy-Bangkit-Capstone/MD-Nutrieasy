package com.capstone.nutrieasy.ui.main.scan.result

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem
import com.capstone.nutrieasy.databinding.FragmentResultBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.IconGravity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val navArgs: ResultFragmentArgs by navArgs()
    private val viewModel by viewModels<ResultFragmentViewModel>()
    private lateinit var auth: FirebaseAuth
    private var adapter: NutrientResultAdapter? = null
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
        setupView()
        setupAction()
        setupState()
    }

    private fun setupView(){
        binding.foodIv.setImageURI(
            navArgs.imageUri.toUri()
        )

        adapter = NutrientResultAdapter(requireContext())
        val layout = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout
    }

    private fun setupAction(){
        binding.minBtn.setOnClickListener {
            viewModel.changeSize(
                viewModel.viewState.value?.size?.minus(1) ?: 0
            )
        }
        binding.addBtn.setOnClickListener {
            viewModel.changeSize(
                viewModel.viewState.value?.size?.plus(1) ?: 0
            )
        }
        binding.trackBtn.setOnClickListener {
            viewModel.track()
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            state.apply {
                if(isLoading){
                    showLoading()
                }else hideLoading()

                if(isTrackLoading){
                    showTrackLoading()
                }else hideTrackLoading()

                if(isDailyNutritionLoading){
                    showDailyNutritionLoading()
                }else hideDailyNutritionLoading()

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
                        val calorie = data?.nutrientsDetailList?.first {
                            it.name.contains("Energy") && it.unit == "kcal"
                        }
                        binding.calorieTv.text = "${calorie?.value?.times(size)}Kcal / ${data?.servingWeightGrams?.times(size)} gram"
                        val nutrientList = data?.nutrientsDetailList?.map {
                            it.copy(value = it.value * size)
                        }
                        binding.amountTv.text = size.toString()
                        adapter?.submitList(nutrientList)
                    }

                }

                when{
                    isTrackError -> {
                        val dialog = MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage(errorMessage)
                            .setIcon(R.drawable.error_24px)
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                        dialog.show()
                    }
                    isTrackSuccess -> {
                        val trackBtn = (binding.trackBtn as MaterialButton)
                        trackBtn.text = getString(R.string.tracked)
                        trackBtn.setIconResource(R.drawable.ic_check)
                        trackBtn.iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
                        trackBtn.isEnabled = false
                    }
                }

                when{
                    isDailyNutritionError -> {
                        Snackbar.make(binding.root, "Failed to get daily nutrition", Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry)) {
                                viewModel.getDailyNutrition()
                            }
                            .show()
                    }
                    isDailyNutritionSuccess -> {
                        for(nutrition in dailyNutritionData!!){
                            setupDailyNutritionView(nutrition)
                        }
                    }
                }
            }
        }
    }

    private fun setupDailyNutritionView(nutrition: TotalIntakeListItem){
        binding.apply {
            when{
                nutrition.name.contains("energy", true) -> {
                    dailyCalorieTv.text = getString(R.string.kcal, nutrition.value.toInt())
                    dailyCaloriePi.max = nutrition.maxValue.toInt()
                    dailyCaloriePi.progress = nutrition.value.toInt()
                }
                nutrition.name.contains("protein", true) -> {
                    dailyProteinTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyProteinPi.max = nutrition.maxValue.toInt()
                    dailyProteinPi.progress = nutrition.value.toInt()
                }
                nutrition.name.contains("fiber", true) -> {
                    dailyFiberTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyFiberPi.max = nutrition.maxValue.toInt()
                    dailyFiberPi.progress = nutrition.value.toInt()
                }
                nutrition.name.contains("sugar", true) -> {
                    dailySugarTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailySugarPi.max = nutrition.maxValue.toInt()
                    dailySugarPi.progress = nutrition.value.toInt()
                }
            }
        }
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTv.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.loadingTv.visibility = View.INVISIBLE
        binding.dataLayout.visibility = View.VISIBLE
    }

    private fun showTrackLoading(){
        binding.trackProgressBar.visibility = View.VISIBLE
        binding.trackBtn.text = ""
    }

    private fun hideTrackLoading(){
        binding.trackProgressBar.visibility = View.INVISIBLE
        binding.trackBtn.text = getString(R.string.track)
    }

    private fun showDailyNutritionLoading(){
        binding.dailyNutritionProgressBar.visibility = View.VISIBLE
        binding.dailyNutritionLayout.visibility = View.INVISIBLE
    }

    private fun hideDailyNutritionLoading(){
        binding.dailyNutritionProgressBar.visibility = View.INVISIBLE
        binding.dailyNutritionLayout.visibility = View.VISIBLE
    }
}