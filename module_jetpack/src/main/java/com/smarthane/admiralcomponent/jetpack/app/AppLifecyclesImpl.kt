package com.smarthane.admiralcomponent.jetpack.app

import android.app.Application
import android.content.Context
import com.smarthane.admiral.core.base.delegate.AppLifecycles

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
class AppLifecyclesImpl : AppLifecycles {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }
}