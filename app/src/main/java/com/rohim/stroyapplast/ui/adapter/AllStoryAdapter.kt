package com.rohim.stroyapplast.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohim.stroyapplast.data.response.ListStoryItem
import com.rohim.stroyapplast.databinding.ItemStoryBinding
import com.rohim.stroyapplast.ui.DetailStoryActivity

class AllStoryAdapter: PagingDataAdapter<ListStoryItem, AllStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

//    interface StoryOnClickListener {
//        fun onClick(story: ListStoryItem)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.imItemStory)
                tvNameUser.text = story.name
            }
            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.DETAIL, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imItemStory, "story_photo"),
                        Pair(binding.tvNameUser, "story_name"),
                    )
                itemView.context.startActivity(moveToDetail, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}