package com.capstone.nutrieasy.ui.main.itemdetail

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.databinding.FragmentItemDetailBinding
import com.capstone.nutrieasy.ui.main.scan.result.NutrientResultAdapter
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit

class ItemDetailFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailBinding
    private var adapter: NutrientResultAdapter? = null
    private val navArgs: ItemDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(navArgs.item)
    }

    private fun setupView(item: HistoryItem){
        Picasso.get().load(item.imageUrl)
            .placeholder(R.drawable.vecteezy_world_food_day_healthy_eating_illustration_green_food_safety_7611496)
            .error(R.drawable.vecteezy_world_food_day_healthy_eating_illustration_green_food_safety_7611496)
            .into(binding.foodIv)
        binding.nameTv.text = item.foodName
        val calorie = item.nutrientsDetailList.first {
            it.name.contains("Energy") && it.unit == "kcal"
        }
        binding.calorieTv.text = "${calorie.value.times(item.servingQty)}Kcal / ${item.servingWeightGrams.times(item.servingQty)}"
        val nutrientList = item.nutrientsDetailList.map {
            it.copy(value = it.value * item.servingQty)
        }
        binding.amountTv.text = getString(R.string.qty, item.servingQty)

        adapter = NutrientResultAdapter(requireContext())
        adapter?.submitList(nutrientList)
        val layout = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layout
    }
}