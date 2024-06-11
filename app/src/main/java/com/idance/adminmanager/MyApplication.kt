package com.idance.adminmanager

import android.app.Application
import com.idance.adminmanager.utils.AppConfig
import com.idance.adminmanager.utils.AppConfigUtil

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfigUtil.appConfig = AppConfig()

    }
}