package com.idance.adminmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.idance.adminmanager.R
import com.idance.adminmanager.databinding.ItemVideoBinding
import com.idance.adminmanager.models.VideoPaid
class VideoComparator : DiffUtil.ItemCallback<VideoPaid>(){
    override fun areItemsTheSame(oldItem: VideoPaid, newItem: VideoPaid): Boolean {
        return oldItem.video_id == newItem.video_id
    }

    override fun areContentsTheSame(oldItem: VideoPaid, newItem: VideoPaid): Boolean {
        return oldItem.video_id == newItem.video_id
    }

}
class DetailAdapter : ListAdapter<VideoPaid, DetailAdapter.DetailVH>(VideoComparator()) {
    class DetailVH(val binding: ItemVideoBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailVH {
        return DetailVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: DetailVH, position: Int) {
        holder.binding.video = getItem(position)
        holder.binding.executePendingBindings()
    }
}