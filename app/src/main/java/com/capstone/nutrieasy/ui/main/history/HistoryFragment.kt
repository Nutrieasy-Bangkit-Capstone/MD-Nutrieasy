package com.capstone.nutrieasy.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.databinding.FoodItemBinding
import com.capstone.nutrieasy.databinding.FragmentHistoryBinding
import com.capstone.nutrieasy.ui.main.userhome.HistoryAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel by viewModels<HistoryFragmentViewModel>()
    private var adapter: HistoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHistory()
        setupView()
        setupAction()
        setupState()
    }

    private fun setupView(){
        binding.apply {
            dateTv.text = if(viewModel.date.isEmpty()){
                getString(R.string.all_history)
            }else viewModel.date

            adapter = HistoryAdapter(
                requireContext(),
                object: HistoryAdapter.Action{
                    override fun onClick(item: HistoryItem, binding: FoodItemBinding) {
                        val directions = HistoryFragmentDirections.actionHistoryFragmentToItemDetailFragment(item)
                        val extras = FragmentNavigatorExtras(
                            binding.foodIv to "itemImage",
                            binding.foodName to "itemName"
                        )
                        if (findNavController().currentDestination?.id == R.id.historyFragment) {
                            findNavController().navigate(directions, extras)
                        }
                    }
                }
            )
            adapter?.submitList(listOf())
            val layout = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layout
        }
    }

    private fun setupAction(){
        binding.calendarBtn.setOnClickListener {
            val dateLong = if(viewModel.long != 0L){
                viewModel.long
            }else MaterialDatePicker.todayInUtcMilliseconds()

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(dateLong)
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatToString = SimpleDateFormat("yyyy MMMM dd", Locale.US)
                val dateText: String = dateFormatToString.format(Date(it))
                binding.dateTv.text = dateText
                viewModel.date = dateText
                viewModel.long = it
                viewModel.getHistory()
            }

            datePicker.show(parentFragmentManager, "tag")
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
                                viewModel.getHistory()
                            }
                            .show()
                    }
                    isSuccess -> {
                        if(data == null){
                            binding.recyclerView.visibility = View.INVISIBLE
                            binding.emptyTv.visibility = View.VISIBLE
                        }else if(data!!.isEmpty()){
                            binding.recyclerView.visibility = View.INVISIBLE
                            binding.emptyTv.visibility = View.VISIBLE
                        }else{
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyTv.visibility = View.INVISIBLE
                            adapter?.submitList(data)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.historyPb.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        binding.historyPb.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }
}