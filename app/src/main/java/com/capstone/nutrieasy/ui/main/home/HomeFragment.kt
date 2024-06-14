package com.capstone.nutrieasy.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.response.HistoryResponse
import com.capstone.nutrieasy.databinding.FragmentHomeBinding
import com.capstone.nutrieasy.ui.adapter.HistoryAdapter
import com.capstone.nutrieasy.ui.adapter.LoadingStateAdapter
import com.capstone.nutrieasy.ui.authorization.AuthorizationActivity
import com.capstone.nutrieasy.ui.main.detail.HistoryDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private val historyViewModel: HistoryViewModel by viewModels()
    @Inject lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        setupView()
        initRecyclerView()
        getData()

        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing = false
            setLoading(true)
            getData()
        }

        historyAdapter.setOnItemClicked(object : HistoryAdapter.OnItemClickListener{
            override fun onItemClicked(id: String) {
                val intent = Intent(requireActivity(), HistoryDetailActivity::class.java)
                intent.putExtra(HistoryDetailActivity.HISTORY_ID, id)
                startActivity(intent)
            }
        })

        return binding?.root
    }

    private fun getData(){
        setLoading(true)
        historyViewModel.getListFruit().observe(viewLifecycleOwner) { responseListStory: PagingData<HistoryResponse> ->
            Log.d("HomeFragment", "Received data: $responseListStory")
            historyAdapter.submitData(lifecycle, responseListStory)
            historyAdapter.addLoadStateListener { listener ->
                if (listener.refresh != LoadState.Loading) {
                    setLoading(false)
                }
                if (listener.refresh is LoadState.Error) {
                    val data = listener.refresh as LoadState.Error
                    Log.e("HomeFragment", "Network error: ${data.error}")
                    if (data.error.message.equals("HTTP 401 Unauthorized")) {
                        Toast.makeText(requireActivity(), "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), AuthorizationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().clear().apply()
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
            this?.rvListFruit?.adapter = historyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    historyAdapter.retry()
                }
            )
        }
    }

    private fun setupView(){
        binding.apply {
            Picasso.get().load(auth.currentUser?.photoUrl)
                .placeholder(R.drawable.account_circle_dark_24px)
                .error(R.drawable.account_circle_dark_24px)
                .into(binding.itemLogo)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

}