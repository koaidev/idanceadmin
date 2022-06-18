package com.idance.adminmanager.models


import androidx.annotation.Keep

@Keep
data class VipUser(
    val amount: String,
    val created_at: String?,
    val id: String,
    val message: String?, //name/sdt/email
    val order_id: String,
    val order_info: String,
    val request_id: String,
    val updated_at: String?,
    val user_id: String
)