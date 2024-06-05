package com.capstone.nutrieasy.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.databinding.FragmentHomeBinding
import com.capstone.nutrieasy.ui.adapter.FruitAdapter
import com.capstone.nutrieasy.ui.adapter.LoadingStateAdapter
import com.capstone.nutrieasy.ui.authorization.AuthorizationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val historyViewModel: HistoryViewModel by viewModels()
    @Inject lateinit var fruitAdapter: FruitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initRecyclerView()
        getData()

        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing = false
            setLoading(true)
            getData()
        }

        fruitAdapter.setOnItemClicked(object : FruitAdapter.OnItemClickListener{
            override fun onItemClicked(id: String) {
                val intent = Intent(requireActivity(), DetailFruitActivity::class.java)
                intent.putExtra(DetailFruitActivity.STORY_ID, id)
                startActivity(intent)
            }
        })

        return binding?.root
    }

    private fun getData(){
        setLoading(true)
        historyViewModel.getListFruit().observe(viewLifecycleOwner) { responseListStory: PagingData<FruitResponse> ->
            fruitAdapter.submitData(lifecycle, responseListStory)
            fruitAdapter.addLoadStateListener { listener ->
                if (listener.refresh != LoadState.Loading) {
                    setLoading(false)
                }
                if (listener.refresh is LoadState.Error) {
                    val data = listener.refresh as LoadState.Error
                    if (data.error.message.equals("HTTP 401 Unauthorized")) {
                        Toast.makeText(requireActivity(), "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), AuthorizationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        requireActivity().getSharedPreferences(requireActivity().packageName, Context.MODE_PRIVATE).edit().clear().apply()
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        binding?.llError?.isVisible = true
                        binding?.tvRetry?.setOnClickListener {
                            binding?.llError?.isVisible = false
                            getData()
                        }
                    }
                    Log.e(HomeFragment::class.java.simpleName, "Error activity ${data.error.message}")
                    Log.e(HomeFragment::class.java.simpleName, "Error activity localized ${data.error.localizedMessage}")
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            this?.rvListFruit?.layoutManager = LinearLayoutManager(requireActivity())
            this?.rvListFruit?.setHasFixedSize(true)
            this?.rvListFruit?.adapter = fruitAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    fruitAdapter.retry()
                }
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}