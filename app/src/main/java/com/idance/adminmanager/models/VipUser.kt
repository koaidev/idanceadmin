package com.idance.adminmanager.models


import androidx.annotation.Keep

@Keep
data class VipUser(
    @Keep
    val orderId: String,
    @Keep
    val requestId: String,
    @Keep
    val amount: String,
    @Keep
    val extraData: String,
    @Keep
    val message: String?,
    @Keep
    val orderInfo: String?,
    @Keep
    val signature: String?,
)