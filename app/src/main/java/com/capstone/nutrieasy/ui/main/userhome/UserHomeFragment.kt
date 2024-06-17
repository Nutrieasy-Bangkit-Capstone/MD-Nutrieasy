package com.capstone.nutrieasy.ui.main.userhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.databinding.FragmentUserHomeBinding
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem
import com.capstone.nutrieasy.databinding.FoodItemBinding
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
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun setupView(){
        binding.apply {
            Picasso.get().load(viewModel.user.photoUrl)
                .placeholder(R.drawable.account_circle_dark_24px)
                .error(R.drawable.account_circle_dark_24px)
                .into(binding.userIv)

            displayNameTv.text = getString(R.string.hello, viewModel.user.displayName)

            adapter = HistoryAdapter(
                requireContext(),
                object: HistoryAdapter.Action{
                    override fun onClick(item: HistoryItem, binding: FoodItemBinding) {
                        val directions = UserHomeFragmentDirections.actionUserHomeFragmentToItemDetailFragment(item)
                        val extras = FragmentNavigatorExtras(
                            binding.foodIv to "itemImage",
                            binding.foodName to "itemName"
                        )
                        if (findNavController().currentDestination?.id == R.id.userHomeFragment) {
                            findNavController().navigate(directions, extras)
                        }
                    }
                }
            )
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
                nutrition.name.contains("energy", true) -> {
                    dailyCalorieTv.text = getString(R.string.kcal, nutrition.value.toInt())
                    dailyCaloriePi.progress = nutrition.value.toInt()
                    dailyCaloriePi.max = nutrition.maxValue.toInt()
                }
                nutrition.name.contains("protein", true) -> {
                    dailyProteinTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyProteinPi.progress = nutrition.value.toInt()
                    dailyProteinPi.max = nutrition.maxValue.toInt()
                }
                nutrition.name.contains("fiber", true) -> {
                    dailyFiberTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailyFiberPi.progress = nutrition.value.toInt()
                    dailyFiberPi.max = nutrition.maxValue.toInt()
                }
                nutrition.name.contains("sugar", true) -> {
                    dailySugarTv.text = getString(R.string.gram, nutrition.value.toInt())
                    dailySugarPi.progress = nutrition.value.toInt()
                    dailySugarPi.max = nutrition.maxValue.toInt()
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