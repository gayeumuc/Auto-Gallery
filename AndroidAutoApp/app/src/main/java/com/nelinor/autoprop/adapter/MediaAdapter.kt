package com.nelinor.autoprop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelinor.autoprop.R
import com.nelinor.autoprop.databinding.ItemMediaBinding
import com.nelinor.autoprop.model.MediaItem

/**
 * RecyclerView adapter that displays a grid of gallery images on the phone.
 * Uses Glide for efficient thumbnail loading.
 */
class MediaAdapter(
    private val onItemClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, MediaAdapter.MediaViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MediaViewHolder(
        private val binding: ItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaItem) {
            Glide.with(binding.ivThumbnail.context)
                .load(item.uri)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                onItemClick(item)
            }

            // Highlight if this item is currently selected/sent to car
            binding.root.isSelected = false
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem) =
                oldItem == newItem
        }
    }
}
