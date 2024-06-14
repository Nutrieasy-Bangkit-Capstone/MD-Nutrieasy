package com.capstone.nutrieasy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.data.response.HistoryResponse
import com.capstone.nutrieasy.databinding.ItemListBinding
import com.capstone.nutrieasy.helper.changeFormatDate
import javax.inject.Inject

class HistoryAdapter@Inject constructor(): PagingDataAdapter<HistoryResponse, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponse>(){
            override fun areItemsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    private lateinit var onItemClickListener: OnItemClickListener


    fun setOnItemClicked(onItemClickListener: Any) {
        this.onItemClickListener = onItemClickListener as OnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindData(item)
        }
    }

    inner class HistoryViewHolder(private val view: ItemListBinding) : RecyclerView.ViewHolder(view.root) {
        fun bindData(item: HistoryResponse) {
            view.tvDate.text = item.createdAt?.let { changeFormatDate(it) }
            view.tvFruitName.text = item.name

            val nutrientsDetail = item.nutrientsDetailList?.joinToString(separator = "\n") { detail ->
                "${detail.name}: ${detail.value}"
            }
            view.tvFruitDetail.text = nutrientsDetail

            Glide.with(view.root)
                .load(item.photoUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view.ivFruitPicture)

            view.cvItemStory.setOnClickListener {
                onItemClickListener.onItemClicked(item.id as String)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

}