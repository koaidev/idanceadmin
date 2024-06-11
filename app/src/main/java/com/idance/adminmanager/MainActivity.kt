package com.idance.adminmanager

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.idance.adminmanager.databinding.ActivityMainBinding
import com.idance.adminmanager.databinding.DialogAddDanceSongToUserBinding
import com.idance.adminmanager.models.VideoPaid
import com.idance.adminmanager.models.VipUser
import com.idance.adminmanager.utils.AppConfigUtil
import com.koaidev.idancesdk.model.SingleDetailsCourse
import com.koaidev.idancesdk.model.SingleDetailsMovie
import com.koaidev.idancesdk.service.ApiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private var listUser = ArrayList<VipUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Firebase.initialize(this)
        setContentView(binding.root)
        adapter = UserAdapter(this)
        binding.rcUser.adapter = adapter

        getListUser()
        binding.search.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Cần nhập tên hoặc email người dùng",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (binding.search.query.toString().contains("@")) {
                        Firebase.firestore.collection("users")
                            .where(Filter.equalTo("email", binding.search.query.toString()))
                            .get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    adapter.submitList(it.result.toObjects(VipUser::class.java))
                                }
                            }
                    } else {

                        Firebase.firestore.collection("users")
                            .orderBy("name").startAt(binding.search.query.toString())
                            .get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    adapter.submitList(it.result.toObjects(VipUser::class.java))
                                }
                            }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.submitList(listUser)
                } else {
                    adapter.submitList(listUser.filter { it ->
                        it.uid?.contains(newText.lowercase()) == true || it.email?.contains(
                            newText.lowercase()
                        ) == true || it.phone?.contains(newText.lowercase()) == true || it.name?.lowercase()
                            ?.contains(newText.lowercase()) == true
                    })
                }
                return true
            }
        })
        setClick()
    }

    private fun setClick() {
        binding.btnAddManual.setOnClickListener {
            val dialog = Dialog(this, R.style.MyDialog)
            val bindingDialog = DialogAddDanceSongToUserBinding.inflate(layoutInflater)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(bindingDialog.root)
            dialog.setCanceledOnTouchOutside(true)
            bindingDialog.button.setOnClickListener {
                kotlin.run {
                    bindingDialog.button.isEnabled = false
                    if (bindingDialog.edtUserId.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Email/ID user không được null.", Toast.LENGTH_SHORT)
                            .show()
                        bindingDialog.button.isEnabled = true
                        return@run
                    }
                    if (bindingDialog.edtVideoId.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Video Id không được null.", Toast.LENGTH_SHORT).show()
                        bindingDialog.button.isEnabled = true
                        return@run
                    }
                    if (bindingDialog.checkSingleMovie.isChecked) {
                        ApiController.getService().getSingleDetailsMovie(
                            apiKey = AppConfigUtil.appConfig.apiKey,
                            authorization = AppConfigUtil.appConfig.authorization,
                            id = bindingDialog.edtVideoId.text.toString(),
                            userId = bindingDialog.edtUserId.text.toString()
                        ).enqueue(object : Callback<SingleDetailsMovie> {
                            override fun onResponse(
                                call: Call<SingleDetailsMovie>,
                                response: Response<SingleDetailsMovie>
                            ) {
                                if (bindingDialog.edtUserId.text.toString().contains("@")) {
                                    Firebase.firestore.collection("users").where(
                                        Filter.equalTo(
                                            "email",
                                            bindingDialog.edtUserId.text.toString().trim()
                                        )
                                    ).limit(1).get().addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val user = it.result.toObjects(VipUser::class.java)[0]
                                            user.uid?.let { it1 ->
                                                Firebase.firestore.collection("videos_paid")
                                                    .document(it1)
                                                    .update(
                                                        "list_video_paid",
                                                        FieldValue.arrayUnion(
                                                            VideoPaid(
                                                                number_can_watch = 1,
                                                                video_id = 1,
                                                                uid = it1,
                                                                name = response.body()?.title,
                                                                thumb = response.body()?.thumbnailUrl,
                                                                status = true
                                                            )
                                                        )
                                                    ).addOnCompleteListener { it2 ->
                                                        if (it2.isSuccessful) {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Thành công",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            dialog.dismiss()
                                                        } else {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Lỗi đã xảy ra.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            bindingDialog.button.isEnabled = true
                                                            dialog.dismiss()

                                                        }
                                                    }
                                            }
                                        }
                                    }

                                } else {
                                    Firebase.firestore.collection("videos_paid")
                                        .document(bindingDialog.edtUserId.text.toString())
                                        .update(
                                            "list_video_paid",
                                            FieldValue.arrayUnion(
                                                VideoPaid(
                                                    number_can_watch = 1,
                                                    video_id = 1,
                                                    uid = bindingDialog.edtUserId.text.toString(),
                                                    name = response.body()?.title,
                                                    thumb = response.body()?.thumbnailUrl,
                                                    status = true
                                                )
                                            )
                                        ).addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Thành công",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                dialog.dismiss()
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Lỗi đã xảy ra.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                bindingDialog.button.isEnabled = true
                                                dialog.dismiss()

                                            }
                                        }
                                }
                            }

                            override fun onFailure(call: Call<SingleDetailsMovie>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Lỗi đã xảy ra.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                bindingDialog.button.isEnabled = true
                                dialog.dismiss()
                            }

                        })
                    } else {
                        ApiController.getService().getSingleDetailsCourse(
                            apiKey = AppConfigUtil.appConfig.apiKey,
                            authorization = AppConfigUtil.appConfig.authorization,
                            id = bindingDialog.edtVideoId.text.toString(),
                            userId = bindingDialog.edtUserId.text.toString()
                        ).enqueue(object : Callback<SingleDetailsCourse> {
                            override fun onResponse(
                                call: Call<SingleDetailsCourse>,
                                response: Response<SingleDetailsCourse>
                            ) {
                                Firebase.firestore.collection("videos_paid")
                                    .document(bindingDialog.edtUserId.text.toString())
                                    .update(
                                        "list_video_paid",
                                        FieldValue.arrayUnion(
                                            VideoPaid(
                                                number_can_watch = 1,
                                                video_id = 1,
                                                uid = bindingDialog.edtUserId.text.toString(),
                                                name = response.body()?.title,
                                                thumb = response.body()?.thumbnailUrl,
                                                status = true
                                            )
                                        )
                                    ).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Thành công",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Lỗi đã xảy ra.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            bindingDialog.button.isEnabled = true
                                            dialog.dismiss()
                                        }
                                    }
                            }

                            override fun onFailure(call: Call<SingleDetailsCourse>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Lỗi đã xảy ra.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                bindingDialog.button.isEnabled = true
                                dialog.dismiss()
                            }

                        })
                    }
                }
            }
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getListUser() {
        Firebase.firestore.collection("users")
            .orderBy("lastPlanDate", Query.Direction.DESCENDING)
            .limit(50)
            .get().addOnCompleteListener {
                if (it.exception != null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Lỗi: ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                if (it.isSuccessful) {
                    val list = it.result.toObjects(VipUser::class.java)
                    listUser.clear()
                    listUser.addAll(list)
                    adapter.submitList(list)
                }
            }

        Firebase.firestore.collection("app_infor").document("information").get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    binding.textView6.text =
                        it.result["totalUser"].toString()
                }
            }
    }
}