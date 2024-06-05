//package com.capstone.nutrieasy.ui.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.capstone.nutrieasy.R
//import com.capstone.nutrieasy.databinding.ItemListBinding
//import com.capstone.nutrieasy.helper.changeFormatDate
//import javax.inject.Inject
//
//class FruitAdapter@Inject constructor(): PagingDataAdapter<FruitResponse, FruitAdapter.FruitViewHolder>(DIFF_CALLBACK) {
//
//    companion object{
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FruitResponse>(){
//            override fun areItemsTheSame(oldItem: FruitResponse, newItem: FruitResponse): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: FruitResponse, newItem: FruitResponse): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//        }
//    }
//
//    private lateinit var onItemClickListener: OnItemClickListener
//
//
//    fun setOnItemClicked(onItemClickListener: Any) {
//        this.onItemClickListener = onItemClickListener as OnItemClickListener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
//        return StoryViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//    }
//
//    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        holder.bindData(getItem(position) ?: FruitResponse())
//    }
//
//    inner class StoryViewHolder(private val view: ItemListBinding) : RecyclerView.ViewHolder(view.root) {
//        fun bindData(item: FruitResponse) {
//            view.tvDate.text = item.createdAt?.let { changeFormatDate(it) }
//            view.tvFruitName.text = item.name
//            view.tvFruitDetail.text = item.description
//
//            Glide.with(view.root)
//                .load(item.photoUrl)
//                .placeholder(R.drawable.img_placeholder)
//                .error(R.drawable.img_error)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(view.ivFruitPicture)
//
//            view.cvItemStory.setOnClickListener {
//                onItemClickListener.onItemClicked(item.id as String)
//            }
//        }
//    }
//
//    interface OnItemClickListener {
//        fun onItemClicked(id: String)
//    }
//
//}