package com.idance.adminmanager.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.idance.adminmanager.api.ApiClient
import com.idance.adminmanager.api.ApiInterface
import com.idance.adminmanager.models.DataItem
import com.idance.adminmanager.models.VipUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _users = MutableLiveData<ArrayList<DataItem>>()
    val users: LiveData<ArrayList<DataItem>>
        get() = _users
    val isValidAmount = MutableLiveData<Boolean>()
    val isValidUserID = MutableLiveData<Boolean>()
    val isValidInfo = MutableLiveData<Boolean>()

    private val api = Retrofit.Builder().baseUrl(ApiClient.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    init {
        isValidAmount.value = true
        isValidUserID.value = true
        isValidInfo.value = true
        getAllUser()
    }

    fun getAllUser() {
        viewModelScope.launch(Dispatchers.IO) {

            api.listVipUser().enqueue(object :
                Callback<com.idance.adminmanager.models.Response> {
                override fun onResponse(
                    call: Call<com.idance.adminmanager.models.Response>,
                    response: Response<com.idance.adminmanager.models.Response>
                ) {
                    if (response.isSuccessful && response.body()!!.data != null) {
                        _users.value = (response.body()!!.data)!!
                        println("ABC: ${response.body()?.data}")
                    }

                }

                override fun onFailure(
                    call: Call<com.idance.adminmanager.models.Response>,
                    t: Throwable
                ) {
                    println("Error on get all user: ${t.message}")
                    Toast.makeText(getApplication(), "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun getUserViaID(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            api.getUserById(userId).enqueue(object : Callback<com.idance.adminmanager.models.Response> {
                override fun onResponse(call: Call<com.idance.adminmanager.models.Response>, response: Response<com.idance.adminmanager.models.Response>) {
                    if (response.isSuccessful) {
                        _users.value!!.clear()
                        _users.value = (response.body()!!.data!!)
                    }
                }

                override fun onFailure(call: Call<com.idance.adminmanager.models.Response>, t: Throwable) {
                    println("Error on get user via id: ${t.message}")
                    Toast.makeText(getApplication(), "Lỗi đã xảy ra!", Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }

    fun setNewVipUser(vipUser: VipUser) {
        viewModelScope.launch(Dispatchers.IO) {
            api.setVipUser(
                vipUser
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println("TEST: ${response.body()}")
                    if (response.isSuccessful) {
                        Toast.makeText(getApplication(), "Thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(getApplication(), "Lỗi đã xảy ra!", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println("TESTERROR: ${t.message}")
                    Toast.makeText(getApplication(), "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }
}