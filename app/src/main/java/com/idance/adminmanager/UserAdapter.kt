package com.idance.adminmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idance.adminmanager.databinding.ItemVipUserBinding
import com.idance.adminmanager.models.VipUser

class UserAdapter(private val users: ArrayList<VipUser>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: ItemVipUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemVipUserBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.binding.vipUser = users[position]
        holder.binding.executePendingBindings()
        holder.binding.btnRenewal.setOnClickListener {

        }
        holder.binding.notifyChange()
    }

    override fun getItemCount(): Int {
        return users.size
    }
}