package com.idance.adminmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.idance.adminmanager.adapter.DetailAdapter
import com.idance.adminmanager.databinding.ActivityDetailBinding
import com.idance.adminmanager.models.VideosPaid

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: DetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = DetailAdapter()
        binding.rcv.adapter = adapter
        intent.getStringExtra("userId")
            ?.let {
                Firebase.firestore.collection("videos_paid").document(it).get()
                    .addOnCompleteListener {it2->
                        if (it2.isSuccessful){
                            adapter.submitList(it2.result.toObject(VideosPaid::class.java)?.list_video_paid)
                        }
                    }
            }
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }
}