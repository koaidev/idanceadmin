package com.idance.adminmanager.api

import com.idance.adminmanager.models.DataItem
import com.idance.adminmanager.models.Response
import com.idance.adminmanager.models.VipUser
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("get_all_vip_user/")
    fun listVipUser(): Call<Response>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("id/{user_id}/")
    fun getUserById(@Path("user_id") user_id: String): Call<Response>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("new_vip_user")
    fun setVipUser(@Body vipUser: VipUser) : Call<Void>
}