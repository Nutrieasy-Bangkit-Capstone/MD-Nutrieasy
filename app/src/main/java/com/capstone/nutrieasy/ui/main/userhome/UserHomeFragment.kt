package com.capstone.nutrieasy.ui.main.userhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentUserHomeBinding
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserHomeFragment : Fragment() {
    private lateinit var binding: FragmentUserHomeBinding
    private val viewModel by viewModels<UserHomeFragmentViewModel>()
    private var adapter: HistoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDailyNutrition()
        viewModel.getHistory()
        setupView()
        setupAction()
        setupState()
    }

    private fun setupView(){
        binding.apply {
            Picasso.get().load(viewModel.user.photoUrl)
                .placeholder(R.drawable.account_circle_dark_24px)
                .error(R.drawable.account_circle_dark_24px)
                .into(binding.userIv)

            displayNameTv.text = getString(R.string.hello, viewModel.user.displayName)

            adapter = HistoryAdapter(requireContext())
            adapter?.submitList(listOf())
            val layout = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layout
        }
    }

    private fun setupAction(){
        binding.historyBtn.setOnClickListener {
            val directions = UserHomeFragmentDirections.actionUserHomeFragmentToHistoryFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setupState(){
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            state.apply {
                if(isDailyNutritionLoading){
                    showDailyNutritionLoading()
                }else hideDailyNutritionLoading()

                if(isHistoryLoading){
                    showHistoryLoading()
                }else hideHistoryLoading()

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

                when{
                    isHistoryError -> {
                        Snackbar.make(binding.root, "Failed to get daily history", Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry)) {
                                viewModel.getHistory()
                            }
                            .show()
                    }
                    isHistorySuccess -> {
                        if(historyData?.isEmpty() == true){
                            binding.emptyTv.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.INVISIBLE
                        }else{
                            binding.emptyTv.visibility = View.INVISIBLE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter?.submitList(historyData)
                        }
                    }
                }
            }
        }
    }

    private fun setupDailyNutritionView(nutrition: TotalIntakeListItem){
        binding.apply {
            when{
                nutrition.name.contains("calorie", true) -> {
                    dailyCalorieTv.text = getString(R.string.kcal, nutrition.value.toInt())
                    dailyCaloriePi.progress = nutrition.value.toInt()
                    dailyCaloriePi.max = 2300
                }
                nutrition.name.contains("protein", true) -> {
                    dailyProteinTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyProteinPi.progress = nutrition.value.toInt()
                    dailyProteinPi.max = 170
                }
                nutrition.name.contains("fiber", true) -> {
                    dailyFiberTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyFiberPi.progress = nutrition.value.toInt()
                    dailyFiberPi.max = 50
                }
                nutrition.name.contains("fat", true) -> {
                    dailyFatTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyFatPi.progress = nutrition.value.toInt()
                    dailyFatPi.max = 150
                }
            }
        }
    }

    private fun showDailyNutritionLoading(){
        binding.dailyNutritionPb.visibility = View.VISIBLE
        binding.dailyNutritionLayout.visibility = View.INVISIBLE
    }

    private fun hideDailyNutritionLoading(){
        binding.dailyNutritionPb.visibility = View.INVISIBLE
        binding.dailyNutritionLayout.visibility = View.VISIBLE
    }

    private fun showHistoryLoading(){
        binding.historyPb.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
    }

    private fun hideHistoryLoading(){
        binding.historyPb.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }
}