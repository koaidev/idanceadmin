package com.idance.adminmanager.models

import com.google.gson.annotations.SerializedName

data class Response(
    val data: ArrayList<DataItem>? = null,
    val status: Int? = null
)

data class DataItem(
    @field:SerializedName("amount")
    val amount: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("signature")
    val signature: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("signature_confirm")
    val signatureConfirm: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("request_id")
    val requestId: String? = null,

    @field:SerializedName("order_info")
    val orderInfo: String? = null
)

