package com.idance.adminmanager

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.idance.adminmanager.databinding.ActivityMainBinding
import com.idance.adminmanager.databinding.DialogSetVipUserBinding
import com.idance.adminmanager.models.VipUser
import com.idance.adminmanager.utils.IdComparator
import com.idance.adminmanager.viewmodel.UserViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var dialogBinding: DialogSetVipUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setRcUser()
        dialogBinding = DialogSetVipUserBinding.inflate(layoutInflater)
        val dialog = Dialog(this, R.style.Theme_IDANCEADMIN)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(dialogBinding.root)
        binding.swipeRefresh.isRefreshing = true
        checkInputValid()
        binding.btnAdd.setOnClickListener {
            dialog.show()
            dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialogBinding.btnAddNew.setOnClickListener {
                if (userViewModel.isValidAmount.value == true
                    && userViewModel.isValidInfo.value == true && userViewModel.isValidUserID.value == true
                ) {

                    val orderId = "ID" + System.currentTimeMillis()
                    val json = JsonObject()
                    json.addProperty("user_id", dialogBinding.ctUserId.editText?.text.toString())

                    val extraData =
                        Base64.getEncoder().encodeToString(json.toString().encodeToByteArray())
                            .toString()

                    println("Extra: $extraData")

                    val vipUser =
                        VipUser(
                            orderId = orderId,
                            requestId = orderId,
                            amount = dialogBinding.ctAmount.editText?.text.toString(),
                            extraData = extraData,
                            signature = "Chuyển khoản null signature",
                            message = "Thanh toán bằng chuyển khoản",
                            orderInfo = dialogBinding.ctUserInfo.editText?.text.toString()
                        )
                    userViewModel.setNewVipUser(vipUser)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đúng dữ liệu.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        setRefresh()
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (binding.search.query.toString().trim().length == 28) {
                    userViewModel.getUserViaID(binding.search.query.toString().trim())
                    true
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "ID người dùng không đúng",
                        Toast.LENGTH_SHORT
                    ).show()
                    true

                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkInputValid() {
        userViewModel.isValidAmount.observe(this) {
            if (it) {
                dialogBinding.ctAmount.error = null

            } else {
                dialogBinding.ctAmount.error = "Số đã thanh toán không khả dụng."
            }
        }

        dialogBinding.ctAmount.editText?.doAfterTextChanged {
            userViewModel.isValidAmount.postValue(
                it.toString() == "50000" ||
                        it.toString() == "45000" || it.toString() == "99000" ||
                        it.toString() == "120000" || it.toString() == "149000" ||
                        it.toString() == "150000"
            )
        }

        //user id
        val sameUserId = "HDJiGUNvzyTj4ms2z6JUO5NOpjl1"
        userViewModel.isValidUserID.observe(this) {
            if (it) {
                dialogBinding.ctUserId.error = null

            } else {
                dialogBinding.ctUserId.error = "User ID không khả dụng."
            }
        }
        dialogBinding.ctUserId.editText?.doAfterTextChanged {
            userViewModel.isValidUserID.postValue(it.toString().length == sameUserId.length)
        }
        //check user info
        userViewModel.isValidInfo.observe(this) {
            if (it) {
                dialogBinding.ctUserInfo.error = null

            } else {
                dialogBinding.ctUserInfo.error = "Thông tin nhập không khả dụng."
            }
        }
        dialogBinding.ctUserInfo.editText?.doAfterTextChanged {
            userViewModel.isValidInfo.postValue(it.toString().length > 3)
        }
    }

    private fun setRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            if (binding.search.query.isNotEmpty()) {
                userViewModel.getUserViaID(binding.search.query.toString())
            } else {
                userViewModel.getAllUser()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRcUser() {
        val userAdapter = UserAdapter(
            userViewModel = userViewModel,
            context = this,
            layoutInflate = layoutInflater
        )
        binding.rcUser.adapter = userAdapter
        userViewModel.users.observe(this) {
            val users = it
            Collections.sort(users, IdComparator())
            userAdapter.users.clear()
            userAdapter.users.addAll(users)
            userAdapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }
    }
}