package com.idance.adminmanager

import android.annotation.SuppressLint
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.nio.charset.Charset
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["exp", "amount"], requireAll = true)
fun setExp(text: TextView, exp: String?, amount: String) {
    if(exp!=null){
        val timeStamp = exp.substring(2).toLong()
        val current = System.currentTimeMillis()
        val timeUse = current - timeStamp
        var expTime: Long = 0
        if(amount=="45000"||amount=="50000"){
            expTime = 2678400000
        }
        if(amount=="99000"||amount=="120000"){
            expTime = 8035200000
        }
        if(amount=="150000"||amount=="149000"){
            expTime = 16070400000
        }
        val conLai = expTime - timeUse
        val days = conLai / 1000 / 86400
        val hours = (conLai % 86400000) / 3600
        val minutes = ((conLai % 86400000) % 3600) / 60
        text.text = "Thời hạn còn: $days ngày $hours giờ $minutes phút."
    }else{
        text.text = "Không xác định"
    }
}

@BindingAdapter("user_id")
fun setUserId(textView: TextView, extraData: String){
    val data: ByteArray = decode(extraData, DEFAULT)
    val text = data.toString( "UTF-8" as Charset)
    textView.text = text
}

@SuppressLint("SetTextI18n")
@BindingAdapter("registerDate")
fun setRegisterDate(text: TextView, exp: String?) {
   if(exp!=null){
       val timeStamp = exp.substring(2).toLong()
       text.text = "Thanh toán: " + Date(timeStamp)
   }else{
       text.text = "Không xác định"
   }
}

@BindingAdapter("sotien")
fun setSoTien(text: TextView, amount: String?) {
    if(amount!=null){

        text.text = amount.plus(" vnđ")
    }else{
        text.text = "Không xác định"
    }
}

