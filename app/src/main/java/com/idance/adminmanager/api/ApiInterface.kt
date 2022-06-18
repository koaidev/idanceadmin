package com.idance.adminmanager.api

import com.idance.adminmanager.models.VipUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("users/")
    fun listVipUser(): Call<List<VipUser>>

    @GET("id/{user_id}/")
    fun getUserById(@Path("user_id") user_id: String): Call<VipUser>

}