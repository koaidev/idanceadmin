package com.idance.adminmanager

import android.annotation.SuppressLint
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.nio.charset.Charset
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["exp", "current_plan"], requireAll = true)
fun setExp(text: TextView, exp: Long?, currentPlan: String) {
    if(exp!=null){
        val current = System.currentTimeMillis()
        val timeUse = current - exp
        var expTime: Long = 0
        if(currentPlan=="vip1"){
            expTime = 2678400000
        }
        if(currentPlan=="vip2"){
            expTime = 8035200000
        }
        if(currentPlan=="vip3"){
            expTime = 16070400000
        }
        val conLai = expTime - timeUse
        val days = (conLai / 1000 / 86400).toInt()
        val hours = ((conLai % 86400000) / 3600/1000).toInt()
        val minutes = (((conLai % 86400000) % 3600) / 60).toInt()
        text.text = "Thời hạn còn: $days ngày $hours giờ $minutes phút."
    }else{
        text.text = "Không xác định"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("registerDate")
fun setRegisterDate(text: TextView, exp: Long?) {
   if(exp!=null){
       text.text = "Ngày tham gia: " + Date(exp)
   }else{
       text.text = "Không xác định"
   }
}


