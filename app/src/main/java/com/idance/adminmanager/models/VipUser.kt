package com.idance.adminmanager.models


import androidx.annotation.Keep

@Keep
data class VipUser(
    @Keep
    val currentPlan: String?,
    @Keep
    val dateCreate: Long?,
    @Keep
    val email: String?,
    @Keep
    val fcmToken: String?,
    @Keep
    val image: String?,
    @Keep
    val lastPlanDate: Long?,
    @Keep
    val name: String?,
    @Keep
    val phone: String?,
    @Keep
    val uid: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null)
}