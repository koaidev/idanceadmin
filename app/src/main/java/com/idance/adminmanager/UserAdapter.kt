package com.idance.adminmanager

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.idance.adminmanager.databinding.DialogChoosePlanBinding
import com.idance.adminmanager.databinding.ItemVipUserBinding
import com.idance.adminmanager.models.VipUser

class UserComparator : DiffUtil.ItemCallback<VipUser>() {
    override fun areItemsTheSame(oldItem: VipUser, newItem: VipUser): Boolean =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: VipUser, newItem: VipUser): Boolean =
        oldItem == newItem

}

class UserAdapter(val activity: Activity) :
    ListAdapter<VipUser, UserAdapter.UserViewHolder>(UserComparator()) {

    class UserViewHolder(val binding: ItemVipUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_vip_user,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.vipUser = getItem(position)
        holder.binding.btnRenewal.setOnClickListener {
            val oopsBinding = DialogChoosePlanBinding.inflate(activity.layoutInflater)
            val dialogNotSupport = Dialog(activity, R.style.MyDialog)
            dialogNotSupport.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogNotSupport.setContentView(oopsBinding.root)
            dialogNotSupport.setCanceledOnTouchOutside(true)
            dialogNotSupport.show()
            oopsBinding.btnOk.setOnClickListener {
                val newPlan = if (oopsBinding.planFree.isChecked) {
                    "free"
                } else if (oopsBinding.planVip1.isChecked) {
                    "vip1"
                } else if (oopsBinding.planVip2.isChecked) {
                    "vip2"
                } else if (oopsBinding.planVip3.isChecked) {
                    "vip3"
                } else {
                    "free"
                }
                val map = mutableMapOf<String, Any?>()
                map["currentPlan"] = newPlan
                map["lastPlanDate"] = System.currentTimeMillis()
                Firebase.firestore.collection("users")
                    .document(getItem(position).uid!!).update(map).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Cập nhật thành công.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                activity,
                                "Lỗi trong quá trình cập nhật.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                dialogNotSupport.dismiss()
            }
        }
        holder.binding.root.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    DetailActivity::class.java
                ).apply {
                    putExtra("userId", getItem(position).uid)
                })
        }
        holder.binding.executePendingBindings()

    }
}