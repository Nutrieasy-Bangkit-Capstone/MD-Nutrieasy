package com.capstone.nutrieasy.ui.main.scan.result

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.api.model.NutrientsDetailListItem
import com.capstone.nutrieasy.databinding.NutrientItemBinding
import com.google.android.material.progressindicator.LinearProgressIndicator

class NutrientResultAdapter(
    private val context: Context
): ListAdapter<NutrientsDetailListItem, NutrientResultAdapter.NutrientResultAdapterViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class NutrientResultAdapterViewHolder(private val binding: NutrientItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: NutrientsDetailListItem, position: Int){
            binding.apply {
                val color = context.getColor(colors[position % colors.size])
                nameTv.text = item.name
                valueTv.text = "${item.value}${item.unit} / 100g"
                valueTv.setTextColor(color)
                progressIndicator.progress = item.value.toInt()
                progressIndicator.max = 100
                progressIndicator.setIndicatorColor(color)
                Log.d("ColorVal", "Position: ${position % colors.size}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): NutrientResultAdapterViewHolder {
        val binding = NutrientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NutrientResultAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: NutrientResultAdapterViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    companion object{
        val colors = arrayOf(
            R.color.brown, R.color.yellow, R.color.green, R.color.purple
        )

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NutrientsDetailListItem>(){
            override fun areItemsTheSame(oldItem: NutrientsDetailListItem, newItem: NutrientsDetailListItem): Boolean {
                return oldItem.attrId == newItem.attrId
            }

            override fun areContentsTheSame(oldItem: NutrientsDetailListItem, newItem: NutrientsDetailListItem): Boolean {
                return oldItem.name == newItem.name && oldItem.value == newItem.value
                        && oldItem.unit == newItem.unit
            }
        }
    }
}