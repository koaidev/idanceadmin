package com.idance.adminmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.idance.adminmanager.databinding.ActivityMainBinding
import com.idance.adminmanager.models.VipUser

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

        binding.search.setOnQueryTextListener(object : OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    adapter.submitList(listUser)
                } else {
                    adapter.submitList(listUser.filter { it ->
                        it.uid?.contains(query.lowercase()) == true || it.email?.contains(
                            query.lowercase()
                        ) == true || it.phone?.contains(query.lowercase()) == true || it.name?.lowercase()
                            ?.contains(query.lowercase()) == true
                    })
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
    }

    @SuppressLint("SetTextI18n")
    private fun getListUser() {
        Firebase.firestore.collection("users")
            .orderBy("lastPlanDate", Query.Direction.DESCENDING)
            .limit(50)
            .get().addOnCompleteListener {
                if (it.exception!=null) {
                    Toast.makeText(this@MainActivity, "Lỗi: ${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                if (it.isSuccessful) {
                    val list = it.result.toObjects(VipUser::class.java)
                    listUser.clear()
                    listUser.addAll(list)
                    binding.textView6.text =
                        "${list.size} (${list.filter { it2 -> it2.currentPlan != "free" }.size} VipUser)"
                    adapter.submitList(list)
                }
            }
    }
}