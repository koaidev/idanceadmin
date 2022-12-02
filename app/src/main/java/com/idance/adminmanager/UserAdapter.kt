package com.idance.adminmanager

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.idance.adminmanager.databinding.DialogConfirmBinding
import com.idance.adminmanager.databinding.ItemVipUserBinding
import com.idance.adminmanager.models.DataItem
import com.idance.adminmanager.models.VipUser
import com.idance.adminmanager.viewmodel.UserViewModel
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(
    val users: ArrayList<DataItem> = arrayListOf(),
    val userViewModel: UserViewModel,
    val context: Context,
    val layoutInflate: LayoutInflater
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

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
        holder.binding.vipUser = users[position]

        holder.binding.btnRenewal.setOnClickListener {
            val dialogConfirm = Dialog(context, R.style.Theme_IDANCEADMIN)
            val dialogBinding = DialogConfirmBinding.inflate(layoutInflate)
            dialogConfirm.setCanceledOnTouchOutside(false)
            dialogConfirm.setContentView(dialogBinding.root)
            dialogConfirm.show()
            dialogBinding.btnConfirm.setOnClickListener {
                val orderId = "ID" + System.currentTimeMillis()
                val amount = users[position].amount.toString()

                val json = JsonObject()
                json.addProperty("user_id", users[position].userId)
                val extraData =
                    Base64.getEncoder().encodeToString(json.toString().encodeToByteArray()).toString()
                val signature = "Chuyển khoản null signature"
                val message = "Thanh toán bằng chuyển khoản"
                val orderInfo = users[position].orderInfo
                val vipUser = VipUser(
                    orderId = orderId,
                    requestId = orderId,
                    message = message,
                    extraData = extraData,
                    signature = signature,
                    orderInfo = orderInfo,
                    amount = amount
                )
                userViewModel.setNewVipUser(vipUser)
            }
            dialogBinding.btnCancel.setOnClickListener {
                dialogConfirm.dismiss()
            }

        }

        holder.binding.executePendingBindings()

    }

    override fun getItemCount(): Int {
        return users.size
    }
}