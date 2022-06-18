package com.idance.adminmanager

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter("exp")
fun setExp(text: TextView, exp: String) {
    val timeStamp = exp.substring(2).toInt()
    val current = System.currentTimeMillis()
    val expDate = current - timeStamp
    val days = expDate / 1000 / 86400
    val hours = (expDate % 86400000) / 3600
    val minutes = ((expDate % 86400000) % 3600) / 60
    text.text = "Thời hạn còn: $days ngày $hours giờ $minutes phút."
}


@SuppressLint("SetTextI18n")
@BindingAdapter("registerDate")
fun setRegisterDate(text: TextView, exp: String) {
    val timeStamp = exp.substring(2).toLong()
    text.text = "Thanh toán ngày: " + Date(timeStamp)
}
