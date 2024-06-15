package com.capstone.nutrieasy.ui.main.userhome

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.data.api.model.NutrientsDetailListItem
import com.capstone.nutrieasy.databinding.FoodItemBinding
import com.squareup.picasso.Picasso

class HistoryAdapter(
    private val context: Context
): ListAdapter<HistoryItem, HistoryAdapter.HistoryAdapterViewHolder>(
DIFF_CALLBACK
) {
    inner class HistoryAdapterViewHolder(
        private val binding: FoodItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: HistoryItem){
            binding.apply {
                val calorie = item.nutrientsDetailList.find {
                    it.name.contains("energy", true) && it.unit == "kcal"
                }
                val protein = item.nutrientsDetailList.find {
                    it.name.contains("protein", true)
                }
                val fiber = item.nutrientsDetailList.find {
                    it.name.contains("fiber", true)
                }
                val fat = item.nutrientsDetailList.find {
                    it.name.contains("fat", true)
                }
                val size = item.servingQty * item.servingWeightGrams

                Picasso.get().load(item.imageUrl)
                    .placeholder(R.drawable.vecteezy_world_food_day_healthy_eating_illustration_green_food_safety_7611496)
                    .error(R.drawable.vecteezy_world_food_day_healthy_eating_illustration_green_food_safety_7611496)
                    .into(binding.foodIv)
                foodName.text = item.foodName.uppercase()
                foodCalorieTv.text = context.getString(R.string.calorie_item, calorie?.value?.toInt())
                foodSizeTv.text = context.getString(R.string.size_item, size)

//                proteinPi.progress = protein?.value?.toInt() ?: 0
                proteinPi.progress = 40
                proteinPi.max = 50 * item.servingQty
                proteinSizeTv.text = context.getString(R.string.size_item, protein?.value?.toInt())

                fiberPi.progress = fiber?.value?.toInt() ?: 0
                fiberPi.max = 50 * item.servingQty
                fiberSizeTv.text = context.getString(R.string.size_item, fiber?.value?.toInt())

                fatPi.progress = fat?.value?.toInt() ?: 0
                fatPi.max = 50 * item.servingQty
                fatSizeTv.text = context.getString(R.string.size_item, fat?.value?.toInt())
            }
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, position: Int): HistoryAdapterViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(container.context), container, false)
        return HistoryAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapterViewHolder, position: Int) {
        holder.bind(
            getItem(position)
        )
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>(){
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.foodName == newItem.foodName && oldItem.imageUrl == newItem.imageUrl
                        && oldItem.createdAt == newItem.createdAt && oldItem.userId == newItem.userId
            }
        }
    }
}